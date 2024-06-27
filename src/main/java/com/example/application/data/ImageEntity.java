package com.example.application.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "visit_id")
    private int visitId;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name="visit_id", nullable=false, updatable=false, insertable=false)
    private KolposkopijaIzmeklejumsEntity kolposkopijaIzmeklejumsEntity;

    }
