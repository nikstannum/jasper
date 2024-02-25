import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class App {
    public static void main(String[] args) throws JRException {
        try {
            generateReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateReport() throws JRException, IOException {
        JasperReport mainReport = JasperCompileManager
                .compileReport("C:\\Users\\myASUS\\eclipse-workspace\\jasper\\src\\main\\resources\\template.jrxml");

        Map<String, Object> mainParameters = new HashMap<>();
        mainParameters.put("text", "Some text");

        JasperDesign tableJasperDesign = createDesign();

        JRMapCollectionDataSource jrDataSource = prepareDataSource();

        JasperReport tableJasperReport = JasperCompileManager
                .compileReport(tableJasperDesign);


        mainParameters.put("subreportParameter", tableJasperReport);
        mainParameters.put("subreportDataSource", jrDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, mainParameters, jrDataSource);

        try (FileOutputStream baos = new FileOutputStream("dynamicReportUPDATE.pdf")) {
            JRPdfExporter jrPdfExporter = new JRPdfExporter();
            jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                    baos));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            jrPdfExporter.setConfiguration(configuration);
            jrPdfExporter.exportReport();

        }
    }

    /**
     * Здесь нам нужно развернуть данные, полученные из хранимой процедуры в
     * список объектов с нужным количеством полей. В качестве примера мы просто
     * создадим тестовые данные.
     *
     * @return JRDataSource.
     */
    private static JRMapCollectionDataSource prepareDataSource() {
        List<Map<String, ?>> preparedData = new ArrayList<>();
        Map<String, Object> map;
        map = new HashMap<>();
        map.put("name", "Первый");
        map.put("value", 10);

        // В реальности нужно будет добавлять необходимые поля, сколько нужно,
        // динамически, в зависимости от параметров и данных.
        preparedData.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "Второй");
        map.put("value", 4);
        // В реальности нужно будет добавлять необходимые поля, сколько нужно,
        // динамически, в зависимости от параметров и данных.
        preparedData.add(map);
        return new JRMapCollectionDataSource(preparedData);
    }

    public static JasperDesign createDesign() throws JRException {
        // Эквивалентно StaticText в JasperStudio
        JRDesignStaticText staticText = null;

        // Эквивалентно TextField в JasperStudio
        JRDesignTextField textField = null;

        // Band. Details, Summary, Title и другие.
        JRDesignBand band = null;

        // Вычисляемое выражение. Для записи значений в JRDesignTextField.
        JRDesignExpression expression = null;

        // Для рисования линий.
        @SuppressWarnings("unused")
        JRDesignLine line = null;

        // Для добавления полей в отчёт.
        JRDesignField field = null;

        // Можно создавать условные стили.
        @SuppressWarnings("unused")
        JRDesignConditionalStyle conditionalStyle = null;

        // Рамка вокруг ячейки.
        JRLineBox lineBox = null;

        final int ROW_HEIGHT = 15; // высота строки (ячейки)
        final int COLUMN_WIDTH = 60; // ширина строки (ячейки)

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("sub_template");
//        jasperDesign.setPageWidth(600); // ширина листа
//        jasperDesign.setPageHeight(500); // высота листа
        jasperDesign.setColumnWidth(COLUMN_WIDTH);
        jasperDesign.setColumnSpacing(0); // расстояние между колонками
//        jasperDesign.setLeftMargin(40); // левое поле страницы
//        jasperDesign.setRightMargin(40); // правое
//        jasperDesign.setTopMargin(40); // верхнее
//        jasperDesign.setBottomMargin(40); // нижнее
        jasperDesign.setIgnorePagination(true); // FIXME need to define
        jasperDesign
                .setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL); // FIXME when there is no data

        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("normal");
        String fontPath = "C:\\Users\\myASUS\\eclipse-workspace\\jasper\\src\\main\\resources\\Times New Roman.ttf";
        normalStyle.setPdfFontName(fontPath);
        normalStyle.setPdfEncoding("Identity-H");
        normalStyle.setPdfEmbedded(true);
        normalStyle.setFontSize(8.5f);

        // определяет рамку ячейки
        lineBox = normalStyle.getLineBox();
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineWidth(0.5f);
        jasperDesign.addStyle(normalStyle);


        JRDesignStyle headerStyle = new JRDesignStyle();
        headerStyle.setName("header");
        headerStyle.setDefault(true);
        headerStyle.setFontName("Times New Roman");
        headerStyle.setFontSize(8.5f);
        headerStyle.setBold(true);
        lineBox = headerStyle.getLineBox();
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineWidth(0.5f);
        jasperDesign.addStyle(headerStyle);

        // Поля отчёта.
        field = new JRDesignField();
        field.setName("name");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("value");
        field.setValueClass(java.lang.Integer.class);
        jasperDesign.addField(field);
        // В случае отчёта с динамическими полями пробегаемся по количеству
        // полей и добавляем JRDesignField для каждого с уникальным именем.


        // Title band
        band = new JRDesignBand();
        // добавляем нужные элементы в band.
        // Можно добавлять JRDesignTextField-ы и JRDesignStaticField-ы,
        // картинки и всё, что угодно. Мы пропустим для простоты.
        jasperDesign.setTitle(band);

        int x;
        int y;

        // Detail band (данные)
        band = new JRDesignBand();
        band.setHeight(ROW_HEIGHT);
        x = 0;
        y = 0;
        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(COLUMN_WIDTH);
        textField.setHeight(ROW_HEIGHT);
        expression = new JRDesignExpression();
        expression.setText("$F{name}");
        textField.setExpression(expression);
        textField.setStyle(normalStyle);
        band.addElement(textField);
        x += textField.getWidth();

        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(COLUMN_WIDTH);
        textField.setHeight(ROW_HEIGHT);
        expression = new JRDesignExpression();
        expression.setText("$F{value}");
        textField.setExpression(expression);
        textField.setStyle(normalStyle);
        band.addElement(textField);
        x += textField.getWidth();


        // DetailsBand добавляется немного странно, да...
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);



        // Column footer
        band = new JRDesignBand();
        jasperDesign.setColumnFooter(band);

        return jasperDesign;
    }
}
