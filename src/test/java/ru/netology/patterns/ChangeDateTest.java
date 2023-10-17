package ru.netology.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ChangeDateTest {
    String language = "ru";

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void successBooking() { //Бронирование с валидными данными
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='success-notification']").shouldBe(visible).shouldHave(text
                ("Встреча успешно запланирована на " + date));
    }

    @Test
    void successRebooking() { //выбор другой даты с валидными данными
        var user = DataHelper.Registration.userGenerator(language);
        var firstDate = DataHelper.dateGenerator((int)(Math.random()*10)+3);
        var secondDate = DataHelper.dateGenerator((int)(Math.random()*10)+3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstDate);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='success-notification']").shouldBe(visible).shouldHave(text
                ("Встреча успешно запланирована на " + firstDate));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondDate);
        $(".button").click();
        $("[data-test-id='replan-notification']").shouldBe(visible).shouldHave(text("У вас уже запланирована встреча на " +
                "другую дату. Перепланировать?"));
        $("[role=button]").click();
        $("[data-test-id='success-notification']").shouldBe(visible).shouldHave(text
                ("Встреча успешно запланирована на " + secondDate));
    }

    @Test
    void invalidCity() { //город - не административный центр
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='city'] input").setValue(DataHelper.invalidCityGenerator(language));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldBe(visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void wrongDate() { //неверно выбрана дата
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10)-7);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id=date] .input_invalid").shouldBe(visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void nameInEnglish() { //имя написано латиницей
        var user = DataHelper.Registration.userGenerator(language);
        var invalidUser = DataHelper.Registration.userGenerator("en");
        var date = DataHelper.dateGenerator((int) (Math.random() * 10)+3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(invalidUser.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldBe(visible)
                .shouldHave(text("Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    void tooShortPhone() { //слишком короткий номер телефона
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='phone'] input").sendKeys(Keys.BACK_SPACE);
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldBe(visible)
                .shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void noTickInCheckbox() { //не поставлена галочка о согласии
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".button").click();
        $("[data-test-id='agreement'] .checkbox__text").shouldBe(visible,
                Duration.ofSeconds(15)).shouldHave(exactText("Я соглашаюсь с условиями обработки и " +
                "использования моих персональных данных"));
    }

    @Test
    void noCity() { //поле ГОРОД не заполнено
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='city'] .input__sub").shouldBe(visible,
                Duration.ofSeconds(15)).shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void noDate() { //поле ДАТА не заполнено
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible,
                Duration.ofSeconds(15)).shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void noName() { //поле ИМЯ не заполнено
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='name'] .input__sub").shouldBe(visible,
                Duration.ofSeconds(15)).shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void noPhone() {
        var user = DataHelper.Registration.userGenerator(language);
        var date = DataHelper.dateGenerator((int) (Math.random() * 10) + 3);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='phone'] .input__sub").shouldBe(visible,
                Duration.ofSeconds(15)).shouldHave(exactText("Поле обязательно для заполнения"));
    }
}