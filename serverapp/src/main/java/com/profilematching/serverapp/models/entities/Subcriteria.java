package com.profilematching.serverapp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_subcriteria")
public class Subcriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 3)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double target;

    @OneToMany(mappedBy = "subcriteria")
    private List<CandidateScore> candidateScores;

    @ManyToOne
    @JoinColumn(
            name = "criteria_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_subcriteria"))
    private Criteria criteria;
}