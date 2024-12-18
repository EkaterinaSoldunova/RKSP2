package com.example.onlinediary_webflux_soldunova.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Table("diary")
public class Diary {
    @Id
    private Long id;
    @Column("topic")
    private String topic;
    @Column("description")
    private String description;

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }
} //Солдунова ИКБО-20-21
