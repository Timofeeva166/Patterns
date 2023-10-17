package ru.netology.patterns;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

    public static String cityGenerator(String language) {
        Faker faker = new Faker(new Locale(language));
        String[] city = new String[]{
                "Абакан", "Анадырь", "Архангельск", "Астрахань", "Барнаул", "Белгород", "Биробиджан", "Благовещенск",
                "Брянск", "Великий Новгород", "Владивосток", "Владикавказ", "Владимир", "Волгоград", "Вологда",
                "Воронеж", "Горно-Алтайск", "Грозный", "Екатеринбург", "Иваново", "Ижевск", "Иркутск", "Йошкар-Ола",
                "Казань", "Калининград", "Калуга", "Кемерово", "Киров", "Кострома", "Краснодар", "Красноярск", "Курган",
                "Курск", "Кызыл", "Липецк", "Магадан", "Магас", "Майкоп", "Махачкала", "Москва", "Мурманск", "Нальчик",
                "Нарьян-Мар", "Нижний Новгород", "Новосибирск", "Омск", "Орёл", "Оренбург", "Пенза", "Пермь",
                "Петрозаводск", "Петропавловск-Камчатский", "Псков", "Ростов-на-Дону", "Рязань", "Салехард", "Самара",
                "Санкт-Петербург", "Саранск", "Саратов", "Севастополь", "Симферополь", "Смоленск", "Ставрополь",
                "Сыктывкар", "Тамбов", "Тверь", "Томск", "Тула", "Тюмень", "Улан-Удэ", "Ульяновск", "Уфа", "Хабаровск",
                "Ханты-Мансийск", "Чебоксары", "Челябинск", "Черкесск", "Чита", "Элиста", "Южно-Сахалинск", "Якутск",
                "Ярославль"};
        return city[faker.random().nextInt(0, city.length - 1)];
    }

    public static String invalidCityGenerator(String language) {
        Faker faker = new Faker(new Locale(language));
        String[] city = new String[]{
                "Коломна", "Обнинск", "Ржев", "Муром", "Троицк", "Тобольск"};
        return city[faker.random().nextInt(0, city.length - 1)];
    }

    public static String dateGenerator(int target) {
        String format = "dd.MM.yyyy";
        String date = LocalDate.now().plusDays(target).format(DateTimeFormatter.ofPattern(format));
        return date;
    }

    public static String nameGenerator(String language) {
        Faker faker = new Faker(new Locale(language));
        String name = faker.name().lastName() + " " + faker.name().firstName();
        return name;
    }

    public static String phoneGenerator(String language) {
        Faker faker = new Faker(new Locale(language));
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static  class Registration {
        private Registration() {}

        public static UserInfo userGenerator(String language) {
            UserInfo info = new UserInfo(cityGenerator(language),
                    nameGenerator(language),phoneGenerator(language));
            return info;
        }
    }
}
