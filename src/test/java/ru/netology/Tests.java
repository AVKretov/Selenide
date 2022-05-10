package ru.netology;

import java.time.*;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class Tests {


    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }


    @Test
    void happyPath() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        //Вставляем завтрашнее число в дату встречи
        $x("//input[@placeholder='Дата встречи']").setValue(LocalDate.now().plusDays(1).toString());
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[text()='Успешно!']").shouldBe(visible, Duration.ofSeconds(15));
        //$x("//*[@data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));

    }
    @Test
    void emptyCity() {

        $x("//input[@placeholder='Дата встречи']").setValue(LocalDate.now().plusDays(1).toString());
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='city']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void emptyDate() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "A");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='date']").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void emptyName() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").setValue(LocalDate.now().plusDays(1).toString());
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='name']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void emptyPhone() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").setValue(LocalDate.now().plusDays(1).toString());
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//*[@data-test-id='agreement']").click();
        $x("//span[text()='Забронировать']").click();
        $x("//*[@data-test-id='phone']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void emptyAgreementCheckBox() {

        $x("//input[@placeholder='Город']").setValue("Москва");
        $x("//input[@placeholder='Дата встречи']").setValue(LocalDate.now().plusDays(1).toString());
        $x("//input[@name='name']").setValue("Иванов Иван");
        $x("//input[@name='phone']").setValue("+79112345678");
        $x("//span[text()='Забронировать']").click();
        $x("//*[contains(@class,'input_invalid')]");
    }

    @Test
    void complexElements() {

        //Ввод 2 букв в поле город, после чего выбор нужного города из выпадающего списка:

        $x("//input[@placeholder='Город']").setValue("Мо").sendKeys(Keys.ARROW_DOWN,
                Keys.ARROW_DOWN,Keys.ARROW_DOWN,Keys.ENTER);


        //Выбор даты на неделю вперёд (начиная от текущей даты) через инструмент календаря:

        //Открытие календаря
        $x("//input[@placeholder='Дата встречи']//following-sibling::span").click();
        //создаем переменную с датой доставки.
        int dayOfDelivery = LocalDate.now().plusDays(7).getDayOfMonth();
        //если через 7 дней будет все тот же месяц , что и сегодня то if
        if (dayOfDelivery > 7) {
            System.out.print(String.format("//*[text()='%s']", dayOfDelivery));
            $x(String.format("//*[text()='%s']", dayOfDelivery)).doubleClick();
        }
        //Если нет.
        else {
            $x("//*[@data-step='1']").click();
            $x(String.format("//*[text()='%s']", dayOfDelivery)).doubleClick();
        }



    }


}