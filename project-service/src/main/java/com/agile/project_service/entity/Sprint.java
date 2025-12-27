package com.agile.project_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "sprints")
@EqualsAndHashCode(callSuper = true)
public class Sprint extends BaseEntity implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(length = 1000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sprint_members",
            joinColumns = @JoinColumn(name = "sprint_id"))
    @Column(name = "user_id")
    private Set<Long> memberIds = new HashSet<>();
}
