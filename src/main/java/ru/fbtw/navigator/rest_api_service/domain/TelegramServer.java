package ru.fbtw.navigator.rest_api_service.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "telegramserver")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramServer {
	/*@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	Long id;*/

	@Id
	String name;

	Integer emptySlotsCount;
	String url;


	@OneToMany(mappedBy = "server",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<TelegramBot> telegramBots;

	@OneToMany(mappedBy = "server")
	Set<Slot> slots;
}
