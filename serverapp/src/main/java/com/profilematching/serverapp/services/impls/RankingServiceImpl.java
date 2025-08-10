package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.responses.*;
import com.profilematching.serverapp.models.entities.*;
import com.profilematching.serverapp.repositories.CandidateRepository;
import com.profilematching.serverapp.repositories.CriteriaRepository;
import com.profilematching.serverapp.repositories.RankingRepository;
import com.profilematching.serverapp.services.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RankingResponse> getAllRankings() {
        return rankingRepository.findAll(Sort.by(Sort.Direction.ASC, "rankingOrder"))
                .stream()
                .map(r -> new RankingResponse(
                        r.getCandidate().getName(),
                        r.getTotalScore(),
                        r.getRankingOrder()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void calculateAllRankings() {
        List<Candidate> candidates = candidateRepository.findAll();

        // Hitung total score untuk setiap kandidat
        List<Ranking> rankings = candidates.stream()
                .map(candidate -> {
                    double totalScore = calculateTotalScore(candidate); // Method sebelumnya
                    return new Ranking(null, totalScore, 0, candidate); // rankingOrder akan diisi nanti
                })
                .sorted(Comparator.comparing(Ranking::getTotalScore).reversed()) // Urutkan desc
                .collect(Collectors.toList());

        // Isi ranking order berdasarkan urutan
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRankingOrder(i + 1); // Ranking dimulai dari 1
        }

        // Simpan semua ranking ke DB (otomatis update kalau sudah ada @OneToOne unik)
        rankingRepository.deleteAll(); // Bersihkan ranking sebelumnya jika perlu
        rankingRepository.saveAll(rankings);
    }

    @Override
    public Double calculateTotalScore(Candidate candidate) {
        Map<String, List<Double>> cfMap = new HashMap<>();
        Map<String, List<Double>> sfMap = new HashMap<>();

        // 1. Proses skor kandidat dan pisahkan CF dan SF
        processCandidateScores(candidate, cfMap, sfMap);

        // 2. Ambil semua bobot kriteria dari database
        Map<String, Double> bobotMap = getBobotKriteriaMap();

        double totalScore = 0.0;

        for (String code : cfMap.keySet()) {
            // 3. Hitung rata-rata CF dan SF
            double cf = calculateAverage(cfMap.get(code));
            double sf = calculateAverage(sfMap.getOrDefault(code, Collections.emptyList()));

            // 4. Hitung skor akhir (60% CF + 40% SF)
            double finalScore = calculateFinalScore(cf, sf);

            // 5. Kalikan dengan bobot kriteria dari database
            double bobot = bobotMap.getOrDefault(code, 0.0);
            totalScore += finalScore * bobot;
        }
        return totalScore;
    }

    @Override
    public List<GapResponse> getAllGapDetails() {
        List<GapResponse> result = new ArrayList<>();

        for (Candidate candidate : candidateRepository.findAll()) {
            for (CandidateScore cs : candidate.getScores()) {
                Subcriteria sub = cs.getSubcriteria();
                double gap = cs.getScore() - sub.getTarget();
                double converted = convertGapToValue(gap);

                GapResponse response = new GapResponse();
                response.setCandidateName(candidate.getName());
                response.setCriteriaName(sub.getCriteria().getName());
                response.setSubcriteriaCode(sub.getCode());
                response.setScore(cs.getScore());
                response.setTarget(sub.getTarget());
                response.setGap(gap);
                response.setConvertedValue(converted);

                result.add(response);
            }
        }

        return result;
    }

    @Override
    public List<CFandSFResponse> getCFandSFDetails() {
        List<CFandSFResponse> result = new ArrayList<>();

        for (Candidate candidate : candidateRepository.findAll()) {
            Map<String, List<Double>> cfMap = new HashMap<>();
            Map<String, List<Double>> sfMap = new HashMap<>();

            processCandidateScores(candidate, cfMap, sfMap);

            for (String code : cfMap.keySet()) {
                double cf = calculateAverage(cfMap.get(code));
                double sf = calculateAverage(sfMap.getOrDefault(code, Collections.emptyList()));
                double finalScore = calculateFinalScore(cf, sf);

                CFandSFResponse response = new CFandSFResponse();
                response.setCandidateName(candidate.getName());
                response.setCriteriaCode(code);
                response.setCf(cf);
                response.setSf(sf);
                response.setFinalScore(finalScore);

                result.add(response);
            }
        }

        return result;
    }

    @Override
    public List<FinalScoreDetailResponse> getFinalScoreDetails() {
        List<FinalScoreDetailResponse> result = new ArrayList<>();
        Map<String, Double> bobotMap = getBobotKriteriaMap();

        for (Candidate candidate : candidateRepository.findAll()) {
            Map<String, List<Double>> cfMap = new HashMap<>();
            Map<String, List<Double>> sfMap = new HashMap<>();

            processCandidateScores(candidate, cfMap, sfMap);

            for (String code : cfMap.keySet()) {
                double cf = calculateAverage(cfMap.get(code));
                double sf = calculateAverage(sfMap.getOrDefault(code, Collections.emptyList()));
                double finalScore = calculateFinalScore(cf, sf);
                double weight = bobotMap.getOrDefault(code, 0.0);
                double finalScoreXWeight = finalScore * weight;

                FinalScoreDetailResponse response = new FinalScoreDetailResponse();
                response.setCandidateName(candidate.getName());
                response.setCriteriaCode(code);
                response.setCf(cf);
                response.setSf(sf);
                response.setFinalScore(finalScore);
                response.setWeight(weight);
                response.setFinalScoreXWeight(finalScoreXWeight);

                result.add(response);
            }
        }

        return result;
    }

    @Override
    public List<TotalFinalScoreResponse> getTotalFinalScores() {
        Map<String, Double> bobotMap = getBobotKriteriaMap();
        List<TotalFinalScoreResponse> result = new ArrayList<>();

        for (Candidate candidate : candidateRepository.findAll()) {
            Map<String, List<Double>> cfMap = new HashMap<>();
            Map<String, List<Double>> sfMap = new HashMap<>();

            processCandidateScores(candidate, cfMap, sfMap);

            double total = 0.0;
            for (String code : cfMap.keySet()) {
                double cf = calculateAverage(cfMap.get(code));
                double sf = calculateAverage(sfMap.getOrDefault(code, Collections.emptyList()));
                double finalScore = calculateFinalScore(cf, sf);
                double bobot = bobotMap.getOrDefault(code, 0.0);
                total += finalScore * bobot;
            }

            result.add(new TotalFinalScoreResponse(candidate.getName(), total));
        }

        return result;
    }

    @Override
    public List<GapConversionResponse> getGapConversions() {
        return Arrays.asList(
            new GapConversionResponse(0, 5.0, "Tidak ada selisih (kompetensi sesuai dengan yang dibutuhkan)"),
            new GapConversionResponse(1, 4.5, "Kompetensi individu kelebihan 1 tingkat/level"),
            new GapConversionResponse(-1, 4.0, "Kompetensi individu kekurangan 1 tingkat/level"),
            new GapConversionResponse(2, 3.5, "Kompetensi individu kelebihan 2 tingkat/level"),
            new GapConversionResponse(-2, 3.0, "Kompetensi individu kekurangan 2 tingkat/level"),
            new GapConversionResponse(3, 2.5, "Kompetensi individu kelebihan 3 tingkat/level"),
            new GapConversionResponse(-3, 2.0, "Kompetensi individu kekurangan 3 tingkat/level"),
            new GapConversionResponse(4, 1.5, "Kompetensi individu kelebihan 4 tingkat/level"),
            new GapConversionResponse(-4, 1.0, "Kompetensi individu kekurangan 4 tingkat/level")
        );
    }

    @Override
    public void processCandidateScores(Candidate candidate, Map<String, List<Double>> cfMap, Map<String, List<Double>> sfMap) {
        for (CandidateScore cs : candidate.getScores()) {
            Subcriteria sub = cs.getSubcriteria();
            double gap = cs.getScore() - sub.getTarget();
            double converted = convertGapToValue(gap);
            String code = sub.getCriteria().getCode();

            if (sub.getType().equalsIgnoreCase("Core Factor")) {
                cfMap.computeIfAbsent(code, k -> new ArrayList<>()).add(converted);
            } else {
                sfMap.computeIfAbsent(code, k -> new ArrayList<>()).add(converted);
            }
        }
    }

    @Override
    public Map<String, Double> getBobotKriteriaMap() {
        Map<String, Double> bobotMap = new HashMap<>();
        for (Criteria criteria : criteriaRepository.findAll()) {
            bobotMap.put(criteria.getCode(), criteria.getWeight());
        }
        return bobotMap;
    }

    @Override
    public Double calculateAverage(List<Double> values) {
        if (values.isEmpty()) return 0.0;
        double sum = 0.0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.size();
    }

    @Override
    public Double calculateFinalScore(Double cf, Double sf) {
        return (cf * 0.6) + (sf * 0.4);
    }

    @Override
    public Double convertGapToValue(Double gap) {
        if (gap == 0) return 5.0;
        else if (gap == 1) return 4.5;
        else if (gap == -1) return 4.0;
        else if (gap == 2) return 3.5;
        else if (gap == -2) return 3.0;
        else if (gap == 3) return 2.5;
        else if (gap == -3) return 2.0;
        else if (gap == 4) return 1.5;
        else if (gap == -4) return 1.0;
        else return 0.0;
    }
}