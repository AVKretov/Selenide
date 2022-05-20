package ru.netology;

import java.time.*;
import java.time.format.DateTimeFormatter;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class Tests {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }



    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "A");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
    }


    @Test
    void happyPath() {

        $x("//input[@placeholder='Город']").setValue("Москва");

        //Вставляем число через 3 дня в дату встречи
        $x("//input[@placeholder='Дата встречи']").setValue(generateDate(5));
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        // Ждем появления текста с нашей датой.
        $("[data-test-id='notification'] .notification__content").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text(generateDate(5)));


    }
    @Test
    void emptyCity() {

        $x("//input[@placeholder='Дата встречи']").setValue(LocalDate.now().plusDays(3).toString());
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='city']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void emptyDate() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='date']").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void emptyName() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").setValue(generateDate(3));
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='name']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void emptyPhone() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").setValue(generateDate(3));
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='phone']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void emptyAgreementCheckBox() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").setValue(generateDate(3));
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//span[text()='Забронировать']").click();
        $x("//*[contains(@class,'input_invalid')]");
    }

    @Test
    void complexElements() {

        //Ввод 2 букв в поле город, после чего выбор нужного города из выпадающего списка:

//        $x("//input[@placeholder='Город']").setValue("Мо").sendKeys(Keys.ARROW_DOWN,
//                Keys.ARROW_DOWN,Keys.ARROW_DOWN,Keys.ENTER);

        $x("//input[@placeholder='Город']").setValue("Мо");
        $x("//*[text()= 'Москва']").click();


        //Выбор даты на неделю вперёд (начиная от текущей даты) через инструмент календаря:

        //Открытие календаря
        $x("//input[@placeholder='Дата встречи']//following-sibling::span").click();
        //создаем переменную с датой доставки.
        int dayOfDelivery = LocalDate.now().plusDays(7).getDayOfMonth(); //получаем число доставки в формате dd
        //если число месяца через 7 дней будет больше 7 , значит через 7 дней будет тот же месяц, что и сейчас > if
        if (dayOfDelivery > 7) {
            $x(String.format("//*[@class='calendar__row']/td[contains(text(), '%s')]", dayOfDelivery)).doubleClick();
        }
        //Если нет.
        else {
            $x("//*[@data-step='1']").click(); //Переходим на след. месяц кнопкой стрелка вправо
            $x(String.format("//*[@class='calendar__row']/td[contains(text(), '%s')]", dayOfDelivery)).doubleClick(); //ищем наше число
        }

        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//span[text()='Забронировать']").click();
        // форматируем нужную нам дату из формата dd-mm-yyyy в формат dd.mm.yyyy
        String europeanDatePattern = "dd.MM.yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);
        String myDate = europeanDateFormatter.format(LocalDate.now().plusDays(7)).toString();

        $x(String.format("//*[text()='%s']", myDate)).shouldBe(visible, Duration.ofSeconds(15));

    }


}