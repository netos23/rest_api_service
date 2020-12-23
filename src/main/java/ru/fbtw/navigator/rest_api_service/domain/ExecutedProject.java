package ru.fbtw.navigator.rest_api_service.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "executed")
public class ExecutedProject {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id")
	Project project;

	@JoinColumn(name = "platforms_platforms")
	@Enumerated(EnumType.STRING)
	Platform platforms;

	Boolean active;

	public ExecutedProject() {
	}
}
