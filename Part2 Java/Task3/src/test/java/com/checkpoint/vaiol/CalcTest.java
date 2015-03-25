package com.checkpoint.vaiol;

import static org.junit.Assert.assertEquals;

import com.checkpoint.vaiol.someCalculator.Calculator;
import com.checkpoint.vaiol.someCalculator.Loader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;

public class CalcTest {

    private List<Calculator> calculators;

    @Before
    public void setUpCalcTest() {
        // создаем и загружаем во временный файл нужные данные для теста
        // весь этот гемор чтобы показать что работает загрузка выгрузка из файла
        String path = Paths.get("").toAbsolutePath() + "\\tmp.dat";
        //содержимое файла:
        String info =   "11 1\n" +
                        "25 5\n" +
                        "234 6\n" +
                        "34 7\n" +
                        "31 4";
        File file = new File(path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.write(info);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            // TODO handle IOExeption
        }

        // загружаем только что записанные данные из файла:
        try {
            calculators = Loader.download(path);
        } catch (IOException e) {
            // TODO handle IOExeption
        }
    }

    @After
    public void tearDownCalcTest() {
        String path = Paths.get("").toAbsolutePath() + "\\tmp.dat";
        new File(path).delete();
    }

    @Test
    public void testCalculate() {
        //сравнение решения calculate() c какими либо известным константными решениями
        assertEquals(new Calculator(1, 1).calculate(), new BigInteger("4")); // known constant solutions
        assertEquals(new Calculator(2, 1).calculate(), new BigInteger("6")); // known constant solutions
        assertEquals(new Calculator(3, 1).calculate(), new BigInteger("22")); // known constant solutions
        assertEquals(new Calculator(31, 1).calculate(), new BigInteger("6442450942")); // known constant solutions

    }

    @Test
    public void testCalcAndThreads() {
        //сравнение решения calculate() c calculateWithThreads()
        //так как из первого теста мы знаем, что calculate() работает правильно
        assertEquals(calculators.size(), 5); // проверяем загрузку из файла
        for(Calculator calculator : calculators) {
            assertEquals(calculator.calculate(), calculator.calculateWithThreads());
        }
    }

    @Test
    public void testCalcAndExecutors() {
        //сравнение решения calculate() c calculateWithExecutor()
        //так как из первого теста мы знаем, что calculate() работает правильно
        assertEquals(calculators.size(), 5); // проверяем загрузку из файла
        for(Calculator calculator : calculators) {
            assertEquals(calculator.calculate(), calculator.calculateWithExecutor());
        }
    }
}
