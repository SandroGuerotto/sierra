package data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Comparator;

import helper.DateFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import view.ClassMember;
import view.ItemEvent;

/**
 * Local Database with observable ArrayLists that contains mock data
 * 
 * @author Sandro Guerotto
 * @since 25.12.2016
 * @version 0.1
 */
public class Database {

	private ObservableList<Request> requests = FXCollections.observableArrayList();
	private ObservableList<Gesuch> gesuche = FXCollections.observableArrayList();
	private ObservableList<ItemEvent> events = FXCollections.observableArrayList();
	private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
	private ObservableList<Mark> marks = FXCollections.observableArrayList();
	private Schoolclass schoolclass;
	private ObservableList<ClassMember> teachers = FXCollections.observableArrayList();
	private ObservableList<Absent> absents = FXCollections.observableArrayList();
	private ObservableList<Person> people = FXCollections.observableArrayList();
	private ObservableList<Subject> subjects = FXCollections.observableArrayList();
	private ObservableList<Notification> notifications = FXCollections.observableArrayList();
	private ObservableList<ItemEvent> upcomingTests = FXCollections.observableArrayList();
	private ObservableList<Appointment> standard = FXCollections.observableArrayList();
	private ObservableList<ItemEvent> news = FXCollections.observableArrayList();
	private ObservableList<String> types = FXCollections.observableArrayList();

	private SimpleStringProperty totalAbsentTime = new SimpleStringProperty();

	private Comparator<? super Appointment> comparatorAppMaxId;
	private Comparator<? super Notification> comparatorNotification;
	private Comparator<? super ItemEvent> comparatorItemEvent;

	/**
	 * initializes all observable ArrayLists
	 */
	public Database() {
		initComparator();
		initListeners();
		initPeople();
		initRequest();
		initGesuch();
		initMarks();
		initClass();
		initTeacher();
		initAbsent();
		iniTotalAbsentTime();
		initSubject();
		initTypes();
		initAppointments();
		initNotification();
		initEvent();
		initNews();
		initTests();
		initStandard();
	}

	private void initTypes() {
		String type = new String("Information");
		types.add(type);
		type = new String("Test");
		types.add(type);
		type = new String("Erinnerung");
		types.add(type);
		type = new String("Hausaufgabe");
		types.add(type);
		type = new String("Lektion");
		types.add(type);
	}

	private void initNews() {
		for (Appointment appointment : appointments.filtered(t -> t.getType().equals(types.get(0)))) {
			ItemEvent item = new ItemEvent(appointment);
			news.add(item);
		}
	}

	private void initTests() {
		for (Appointment appointment : appointments.filtered(t -> t.getType().equals(types.get(1)))) {
			upcomingTests.add(new ItemEvent(appointment));
		}
	}

	private void initNotification() {
		notifications.add(new Notification(appointments.get(0)));
	}

	private void initSubject() {
		Subject subject = new Subject(1, "Mathematik", "#00ff00");
		subjects.add(subject);
		subject = new Subject(2, "Franz�sisch", "#001100");
		subjects.add(subject);
		subject = new Subject(4, "Deutsch", "#00f100");
		subjects.add(subject);
		subject = new Subject(5, "Englisch", "#01ff01");
		subjects.add(subject);
		subject = new Subject(6, "Chemie", "#ff00ff");
		subjects.add(subject);
		subject = new Subject(7, "Geschichte", "#ff22ff");
		subjects.add(subject);
	}

	private void initPeople() {
		Person person = new Person(1, "Dunois", "Maximori", "02.11.2016", "078 245 02 21", "maximori.dunois@gmail.com",
				"Spetklasse AP14a", false, false, "1234", "maximori.dunois");
		people.add(person);
		person = new Person(3, "Henrich", "Max", "05.06.1999", "078 205 25 23", "m.henrich@gmail.com",
				"Ersatzklassen: BI14a", false, true, "1234", "max.henrich");
		people.add(person);
		person = new Person(4, "Kalt", "Jean", "15.08.1999", "078 298 45 78", "jean.kalt@gmail.com",
				"Ersatzklassen: BI14a", false, false, "1234", "jean.kalt");
		people.add(person);
		person = new Person(5, "Bauer", "Dieter", "23.09.2001", "078 852 23 01", "b.d2001@hotmail.com",
				"Ersatzklassen: AP14b", false, false, "1234", "dieter.bauer");
		people.add(person);
		// teacher
		person = new Person(2, "Streng", "Manfred", "12.02.1975", "079 352 64 87", "manfred.streng@schule.ch",
				"Deutsch, Englisch, Mathematik", true, false, "1234", "manfred.streng");
		people.add(person);
		person = new Person(6, "Klainfus", "Benjamin", "19.10.1968", "079 253 78 02", "benjamin.klainfus@schule.ch",
				"Geschichte, Biologie, Naturwissenschaft", true, false, "1234", "benjamin.klainfus");
		people.add(person);
	}

	private void initAbsent() {
		Absent absent = new Absent(1, "01.01.2017 08:00", "02.01.2017 12:00", "Krank", false, "J2DxrO3r");
		absents.add(absent);
		absent = new Absent(2, "28.03.2017 10:00", "28.03.2017 19:00", "Arzt", true, "COmka7oR");
		absents.add(absent);
	}

	private void initTeacher() {
		for (Person person : people.filtered(Person::isTeacher)) {
			teachers.add(new ClassMember(person));
		}
	}

	private void initClass() {
		ObservableList<ClassMember> memberList = FXCollections.observableArrayList();
		ClassMember member = new ClassMember(people.filtered(t -> t.getId() == 2).get(0));
		member.setIsClassTeacher(true);
		memberList.add(member);
		member = new ClassMember(people.get(0));
		memberList.add(member);
		member = new ClassMember(people.get(1));
		memberList.add(member);
		member = new ClassMember(people.get(2));
		memberList.add(member);
		member = new ClassMember(people.get(3));
		memberList.add(member);
		schoolclass = new Schoolclass(1, memberList.filtered(t -> !t.isTeacher()).size(), "AP14a");

		schoolclass.setMemebers(memberList);
	}

	private void initMarks() {
		Mark mark = new Mark(1, "01.01.2017", "Mathematik", "Bruchrechnen", "2", "4.85", "4.2");
		marks.add(mark);
		mark = new Mark(2, "28.12.2016", "Deutsch", "Gedicht", "1", "4.2", "5");
		marks.add(mark);
	}

	private void initRequest() {
		Request request = new Request(1, "25.12.2016", "Aufgrund von Gr�nden", "OK");
		requests.add(request);
	}

	private void initGesuch() {
		Gesuch gesuch = new Gesuch(1, "10.02.2016", "Sommerferienverl�ngerung", "Ich will frei haben!", "XX");
		gesuche.add(gesuch);
	}

	private void initEvent() {
		for (Appointment appointment : appointments.filtered(t -> !t.getType().equals(types.get(0)))) {
			ItemEvent itemEvent = new ItemEvent(appointment);
			events.add(itemEvent);
		}
	}

	public ObservableList<Request> getRequests() {
		return requests;
	}

	public ObservableList<Gesuch> getGesuche() {
		return gesuche;
	}

	public ObservableList<ItemEvent> getTasks() {
		return events;
	}

	public ObservableList<Appointment> getAppointments() {
		return appointments;
	}

	public ObservableList<Mark> getMarks() {
		return marks;
	}

	public Schoolclass getSchoolclass() {
		return schoolclass;
	}

	public ObservableList<ClassMember> getMyTeachers() {
		return teachers;
	}

	public ObservableList<Absent> getAbsents() {
		return absents;
	}

	public ObservableList<Person> getPeople() {
		return people;
	}

	public ObservableList<Subject> getSubjects() {
		return subjects;
	}

	public ObservableList<Notification> getNotifications() {
		return notifications;
	}

	public ObservableList<ItemEvent> getUpcomingTests() {
		return upcomingTests;
	}

	public ObservableList<Appointment> getStandardTable() {
		return standard;
	}

	public ObservableList<ItemEvent> getNews() {
		return news;
	}

	public ObservableList<String> getTypes(){
		return types;
	}
	public void addRequest(Request request) {
		requests.add(request);
	}

	public void addAppointment(Appointment appointment) {
		appointment.setId(appointments.stream().max(comparatorAppMaxId).get().getId() + 1);
		if (appointment.getType().equals(types.get(0))) {
			news.add(new ItemEvent(appointment));
		} else {
			events.add(new ItemEvent(appointment));
		}
		appointments.add(appointment);
	}

	public void addGesuch(Gesuch gesuch) {
		gesuche.add(gesuch);
	}

	public void addAbsent(Absent absent) {
		absents.add(absent);
	}

	private void initAppointments() {
		Appointment appointment;
		appointment = new Appointment(2, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 30)),
				LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 25)), "Test: Bruchrechnen",
				"Lernziele 1-4 \n" + "Einfaches Rechnen 1: S12 - S24 \n" + "Zeit: 45min", subjects.get(0),
				people.get(1), types.get(1));
		appointments.add(appointment);
		appointment = new Appointment(3, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30)),
				LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 30)), "Funktionen", "Mathebuch S5 1.-5.",
				subjects.get(0), people.get(1), types.get(3));
		appointments.add(appointment);
		appointment = new Appointment(4, LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(9, 00)),
				LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(13, 30)), "Unit� 5 lesen",
				"Unit� 5 S8 lesen", subjects.get(1), people.get(1), types.get(3));
		appointments.add(appointment);
		appointment = new Appointment(5, LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(19, 00)),
				LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(21, 30)), "Neues Jahr - bessere Noten",
				"Wichtige Theman zum neuen Jahr", new Subject(3, "Infoabend", "#09544A"), null, types.get(0));
		appointments.add(appointment);
	}

	public void deleteAppointment(Appointment old) {
		appointments.remove(old);
		// delete from news
		if (news.filtered(t -> t.getAppointment() == old).size() > 0) {
			news.remove(news.filtered(t -> t.getAppointment() == old).get(0));
		}
		// delete from event list
		if (events.filtered(t -> t.getAppointment() == old).size() > 0) {
			events.remove(events.filtered(t -> t.getAppointment() == old).get(0));
		}
		// delete from notification
		if (notifications.filtered(t -> t.getAppointment() == old).size() > 0) {
			notifications.remove(notifications.filtered(t -> t.getAppointment() == old).get(0));
		}
		if (upcomingTests.filtered(t -> t.getAppointment() == old).size() > 0) {
			upcomingTests.remove(upcomingTests.filtered(t -> t.getAppointment() == old).get(0));
		}

	}

	private void initStandard() {
		// Get calendar set to current date and time
		Calendar c = Calendar.getInstance();
		// Set the calendar to monday of the current week
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// convert date to localdate
		LocalDate monday = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		// All Appointments on monday
		Appointment appointmentMonday = new Appointment(200, LocalDateTime.of(monday, LocalTime.of(7, 30)),
				LocalDateTime.of(monday, LocalTime.of(9, 10)), subjects.get(0).getName(), "", 
				subjects.get(0), people.get(1), types.get(4));
		Appointment appointmentMonday1 = new Appointment(201, LocalDateTime.of(monday, LocalTime.of(9, 40)),
				LocalDateTime.of(monday, LocalTime.of(11, 10)), subjects.get(4).getName(), "", 
				subjects.get(4), people.get(1),	types.get(4));
		Appointment appointmentMonday2 = new Appointment(202, LocalDateTime.of(monday, LocalTime.of(11, 10)),
				LocalDateTime.of(monday, LocalTime.of(12, 10)), subjects.get(3).getName(), "" , 
				subjects.get(3), people.get(1), types.get(4));
		Appointment appointmentMonday3 = new Appointment(203, LocalDateTime.of(monday, LocalTime.of(13, 30)),
				LocalDateTime.of(monday, LocalTime.of(14, 45)), subjects.get(2).getName(), "", 
				subjects.get(2), people.get(1),	types.get(4));
		Appointment appointmentMonday4 = new Appointment(204, LocalDateTime.of(monday, LocalTime.of(14, 50)),
				LocalDateTime.of(monday, LocalTime.of(16, 00)), subjects.get(5).getName(), "", 
				subjects.get(5), people.get(1), types.get(4));

		// All Appointments on Tuesday
		Appointment appointmentTuesday = new Appointment(205, LocalDateTime.of(monday.plusDays(1), LocalTime.of(7, 30)),
				LocalDateTime.of(monday.plusDays(1), LocalTime.of(8, 30)), subjects.get(1).getName(), "", 
				subjects.get(1), people.get(1), types.get(4));
		Appointment appointmentTuesday1 = new Appointment(206, LocalDateTime.of(monday.plusDays(1), LocalTime.of(8, 30)),
				LocalDateTime.of(monday.plusDays(1), LocalTime.of(10, 00)), subjects.get(3).getName(), "", 
				subjects.get(3), people.get(1), types.get(4));
		Appointment appointmentTuesday2 = new Appointment(207, LocalDateTime.of(monday.plusDays(1), LocalTime.of(10, 30)),
				LocalDateTime.of(monday.plusDays(1), LocalTime.of(12, 10)), subjects.get(5).getName(), "", 
				subjects.get(5), people.get(1), types.get(4));
		Appointment appointmentTuesday3 = new Appointment(208, LocalDateTime.of(monday.plusDays(1), LocalTime.of(13, 30)),
				LocalDateTime.of(monday.plusDays(1), LocalTime.of(14, 45)), subjects.get(0).getName(), "" , 
				subjects.get(0), people.get(1), types.get(4));
		Appointment appointmentTuesday4 = new Appointment(209, LocalDateTime.of(monday.plusDays(1), LocalTime.of(14, 50)),
				LocalDateTime.of(monday.plusDays(1), LocalTime.of(16, 00)), subjects.get(2).getName(), "", 
				subjects.get(2), people.get(1), types.get(4));

		// All Appointments on Wednesday
		Appointment appointmentWednesday = new Appointment(210, LocalDateTime.of(monday.plusDays(2), LocalTime.of(7, 30)),
				LocalDateTime.of(monday.plusDays(2), LocalTime.of(9, 10)), subjects.get(4).getName(),"", 
				subjects.get(4), people.get(1), types.get(4));
		Appointment appointmentWednesday1 = new Appointment(211, LocalDateTime.of(monday.plusDays(2), LocalTime.of(9, 40)),
				LocalDateTime.of(monday.plusDays(2), LocalTime.of(11, 10)), subjects.get(0).getName(), "", 
				subjects.get(0), people.get(1), types.get(4));
		Appointment appointmentWednesday2 = new Appointment(212, LocalDateTime.of(monday.plusDays(2), LocalTime.of(11, 10)),
				LocalDateTime.of(monday.plusDays(2), LocalTime.of(12, 10)), subjects.get(2).getName(), "", 
				subjects.get(2), people.get(1), types.get(4));

		// All Appointments on Thursday
		Appointment appointmentThursday = new Appointment(213, LocalDateTime.of(monday.plusDays(3), LocalTime.of(8, 10)),
				LocalDateTime.of(monday.plusDays(3), LocalTime.of(9, 10)), subjects.get(3).getName(), "", 
				subjects.get(3), people.get(1), types.get(4));
		Appointment appointmentThursday1 = new Appointment(214, LocalDateTime.of(monday.plusDays(3), LocalTime.of(9, 40)),
				LocalDateTime.of(monday.plusDays(3), LocalTime.of(11, 10)), subjects.get(1).getName(), "",
				subjects.get(1), people.get(1), types.get(4));
		Appointment appointmentThursday2 = new Appointment(215, LocalDateTime.of(monday.plusDays(3), LocalTime.of(11, 10)),
				LocalDateTime.of(monday.plusDays(3), LocalTime.of(12, 10)),	subjects.get(0).getName(), "" , 
				subjects.get(0), people.get(1), types.get(4));
		Appointment appointmentThursday3 = new Appointment(216, LocalDateTime.of(monday.plusDays(3), LocalTime.of(13, 30)),
				LocalDateTime.of(monday.plusDays(3), LocalTime.of(16, 00)), subjects.get(4).getName(), "", 
				subjects.get(4), people.get(1), types.get(4));

		// All Appointments on Friday
		Appointment appointmentFriday = new Appointment(217, LocalDateTime.of(monday.plusDays(4), LocalTime.of(8, 10)),
				LocalDateTime.of(monday.plusDays(4), LocalTime.of(9, 10)), subjects.get(2).getName(), "", 
				subjects.get(2), people.get(1), types.get(4));
		Appointment appointmentFriday1 = new Appointment(218, LocalDateTime.of(monday.plusDays(4), LocalTime.of(9, 40)),
				LocalDateTime.of(monday.plusDays(4), LocalTime.of(11, 10)), subjects.get(0).getName(), "", 
				subjects.get(0), people.get(1), types.get(4));
		Appointment appointmentFriday2 = new Appointment(219, LocalDateTime.of(monday.plusDays(4), LocalTime.of(11, 10)),
				LocalDateTime.of(monday.plusDays(4), LocalTime.of(12, 10)), subjects.get(3).getName(), "" , 
				subjects.get(3), people.get(1), types.get(4));
		Appointment appointmentFriday3 = new Appointment(220, LocalDateTime.of(monday.plusDays(4), LocalTime.of(13, 30)),
				LocalDateTime.of(monday.plusDays(4), LocalTime.of(15, 15)), subjects.get(5).getName(), "" , 
				subjects.get(5), people.get(1), types.get(4));

		// Add all Appointments
		standard.addAll(appointmentMonday, appointmentMonday1, appointmentMonday2, appointmentMonday3, appointmentMonday4);
		standard.addAll(appointmentTuesday, appointmentTuesday1, appointmentTuesday2, appointmentTuesday3, appointmentTuesday4);
		standard.addAll(appointmentWednesday, appointmentWednesday1, appointmentWednesday2);
		standard.addAll(appointmentThursday, appointmentThursday1, appointmentThursday2, appointmentThursday3);
		standard.addAll(appointmentFriday, appointmentFriday1, appointmentFriday2, appointmentFriday3);

	}

	private void iniTotalAbsentTime() {
		int difdays = 0, difHours = 0;
		for (Absent absent : this.getAbsents()) {
			LocalDateTime to = DateFormatter.StringToLocalDateTime(absent.getDateto());
			LocalDateTime from = DateFormatter.StringToLocalDateTime(absent.getDatefrom());
			difdays += DateFormatter.differenceInDays(from, to);
			difHours += DateFormatter.differenceInHours(from, to);
		}
		while (difHours > 24) {
			difdays++;
			difHours -= 24;
		}
		if (difdays == 0 && difHours == 0) {
			totalAbsentTime.set("Keine Absenz");
		} else {
			totalAbsentTime.set(Integer.toString(difdays) + " Tage " + Integer.toString(difHours) + " Stunden");
		}
	}

	public SimpleStringProperty getTotalAbsentTime() {
		return totalAbsentTime;
	}

	private void initListeners() {
		appointments.addListener(new ListChangeListener<Appointment>() {
			@Override
			public void onChanged(Change<? extends Appointment> c) {
				FXCollections.sort(events, comparatorItemEvent);
				FXCollections.sort(news, comparatorItemEvent);
				FXCollections.sort(notifications, comparatorNotification);
			}
		});
		absents.addListener(new ListChangeListener<Absent>() {
			@Override
			public void onChanged(Change<? extends Absent> c) {
				iniTotalAbsentTime();
			}

		});
	}

	private void initComparator() {
		comparatorAppMaxId = new Comparator<Appointment>() {
			@Override
			public int compare(Appointment o1, Appointment o2) {
				Integer id1 = o1.getId();
				Integer id2 = o2.getId();
				return id1.compareTo(id2);
			}
		};
		comparatorNotification = new Comparator<Notification>() {
			@Override
			public int compare(Notification o1, Notification o2) {
				return o1.getAppointment().getStartLocalDateTime()
						.compareTo(o2.getAppointment().getStartLocalDateTime());
			}
		};
		comparatorItemEvent = new Comparator<ItemEvent>() {
			@Override
			public int compare(ItemEvent o1, ItemEvent o2) {
				return o1.getAppointment().getStartLocalDateTime()
						.compareTo(o2.getAppointment().getStartLocalDateTime());
			}
		};
	}
}
