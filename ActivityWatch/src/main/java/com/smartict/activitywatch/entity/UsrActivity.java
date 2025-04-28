    package com.smartict.activitywatch.entity;


    import jakarta.persistence.*;
    import lombok.Data;
    import org.hibernate.annotations.CreationTimestamp;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "usrActivity")
    @Data
    public class UsrActivity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "fkUserId", nullable = false)
        private Usr usr;

        @ManyToOne
        @JoinColumn(name = "fkActivityId", nullable = false)
        private Activity activity;

        @CreationTimestamp
        @Column(nullable = false)
        private LocalDateTime date;


        @Column
        private boolean isAfk;






    }
