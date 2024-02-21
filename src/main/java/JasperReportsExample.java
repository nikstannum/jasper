import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.view.JasperViewer;

public class JasperReportsExample {
    public static void main(String[] args) {
        try {
            // Создание объекта JasperDesign
            JasperDesign jasperDesign = new JasperDesign();
            jasperDesign.setName("TableExample");

            // Создание DetailBand
            JRDesignBand detailBand = new JRDesignBand();
            detailBand.setHeight(300);

            // Создание списка ячеек таблицы
            List<JRDesignRectangle> cells = new ArrayList<>();
            int columnCount = 3; // Количество колонок
            int rowCount = 5; // Количество строк
            int cellWidth = 500 / columnCount; // Ширина ячейки
            int cellHeight = 200 / rowCount; // Высота ячейки

            // Создание ячеек таблицы
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    JRDesignRectangle cell = new JRDesignRectangle();
                    cell.setX(j * cellWidth);
                    cell.setY(i * cellHeight);
                    cell.setWidth(cellWidth);
                    cell.setHeight(cellHeight);
                    cell.setBackcolor(Color.WHITE);

                    cells.add(cell);


//                    // Добавление границ ячейки
                    JRDesignLine topLine = new JRDesignLine();
                    topLine.setX(0);
                    topLine.setY(0);
                    topLine.setWidth(cellWidth);
                    topLine.setHeight(0);
                    topLine.setWidth(1);

                    JRDesignLine leftLine = new JRDesignLine();
                    leftLine.setX(0);
                    leftLine.setY(0);
                    leftLine.setWidth(0);
                    leftLine.setHeight(cellHeight);
                    leftLine.setWidth(1);

                    JRDesignLine rightLine = new JRDesignLine();
                    rightLine.setX(cellWidth);
                    rightLine.setY(0);
                    rightLine.setWidth(0);
                    rightLine.setHeight(cellHeight);
                    rightLine.setWidth(1);

                    JRDesignLine bottomLine = new JRDesignLine();
                    bottomLine.setX(0);
                    bottomLine.setY(cellHeight);
                    bottomLine.setWidth(cellWidth);
                    bottomLine.setHeight(0);
                    bottomLine.setWidth(1);
//
//                    // Создание элементной группы для границ
                    JRDesignElementGroup elementGroup = new JRDesignElementGroup();
                    elementGroup.addElement(topLine);
                    elementGroup.addElement(leftLine);
                    elementGroup.addElement(rightLine);
                    elementGroup.addElement(bottomLine);


                    // Добавление текста внутри ячейки
                    JRDesignStaticText staticText = new JRDesignStaticText();
                    staticText.setForecolor(Color.BLACK);
                    staticText.setX(5); // Отступ от левого края ячейки
                    staticText.setY(5); // Отступ от верхнего края ячейки
                    staticText.setWidth(cellWidth - 10); // Ширина текстового элемента
                    staticText.setHeight(cellHeight - 10); // Высота текстового элемента
                    staticText.setText("TEST"); // Текст для отображения в ячейке

                    elementGroup.addElement(staticText);

                    // Установка элементной группы в ячейку
                    cell.setElementGroup(elementGroup);

                }
            }

            // Добавление ячеек в DetailBand
            for (JRDesignRectangle cell : cells) {
                detailBand.addElement(cell);
            }

            // Установка DetailBand в JasperDesign
            jasperDesign.setBackground(detailBand);

            // Компиляция JasperDesign в JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);


            // Генерация отчета
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());


            // Экспорт отчета в PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}