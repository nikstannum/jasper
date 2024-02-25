import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
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

        List<Map<String, Object>> notSearchedNotChecked = getNotSearchedNotChecked(1);

        HashMap<String, Class<?>> columnNameClassHeaders = getColumnHeaders(notSearchedNotChecked);

        JasperDesign tableJasperDesign = createDesign(columnNameClassHeaders);

        JRMapCollectionDataSource jrDataSource = prepareDataSource(notSearchedNotChecked);

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
    private static JRMapCollectionDataSource prepareDataSource(List<Map<String, Object>> notSearchedNotChecked) {
        List<Map<String, ?>> preparedData = new ArrayList<>(notSearchedNotChecked);
        return new JRMapCollectionDataSource(preparedData);
    }

    public static JasperDesign createDesign(Map<String, Class<?>> columnNameClass) throws JRException {
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

        int x;
        int y;

        x = 0;
        y = 0;

        // column headers
        // Заголовки колонок.
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(ROW_HEIGHT);

        for (Entry<String, Class<?>> columnName : columnNameClass.entrySet()) {
            staticText = new JRDesignStaticText();
            staticText.setX(x);
            staticText.setY(y);
            staticText.setWidth(COLUMN_WIDTH);
            staticText.setHeight(ROW_HEIGHT);
            staticText.setStyle(headerStyle);
            staticText.setText(columnName.getKey());

            columnHeaderBand.addElement(staticText);

            x += staticText.getWidth();
        }

        jasperDesign.setColumnHeader(columnHeaderBand);



        x = 0;
        y = 0;
        // Detail band (данные)
        band = new JRDesignBand();
        band.setHeight(ROW_HEIGHT);
        // Поля отчёта.
        for (Entry<String, Class<?>> columnClass : columnNameClass.entrySet()) {
            field = new JRDesignField();
            String columnName = columnClass.getKey();
            field.setName(columnName);
            Class<?> columnValueClass = columnClass.getValue();
            field.setValueClass(columnValueClass);
            jasperDesign.addField(field);

            textField = new JRDesignTextField();
            textField.setX(x);
            textField.setY(y);
            textField.setWidth(COLUMN_WIDTH);
            textField.setHeight(ROW_HEIGHT);
            expression = new JRDesignExpression();
            expression.setText("$F{" + columnName + "}");
            textField.setExpression(expression);
            textField.setStyle(normalStyle);
            band.addElement(textField);
            x += textField.getWidth();
        }

        // DetailsBand добавляется немного странно, да...
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);


        // Column footer
//        band = new JRDesignBand();
//        jasperDesign.setColumnFooter(band);

        return jasperDesign;
    }

    private static HashMap<String, Class<?>> getColumnHeaders(List<Map<String, Object>> notSearchedNotChecked) {
        return notSearchedNotChecked.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors
                        .toMap(Entry::getKey, entry -> entry.getValue().getClass(), (existingVal, newVal) -> existingVal, HashMap::new));
    }

    private static List<Map<String, Object>> getNotSearchedNotChecked(int batch) {
        List<Map<String, Object>> list = new ArrayList<>();
        String req1 = "req1";
        String req2 = "req2";
        String req3 = "req3";
        String req4 = "req4";
        String req5 = "req5";
        String req6 = "req6";

        String reqVal1 = "val1";
        String reqVal2 = "val2";
        String reqVal3 = "val3";
        String reqVal4 = "val4";
        String reqVal5 = "val5";
        String reqVal6 = "val6";


        Map<String, Object> map1 = new HashMap<>();
        map1.put(req1, reqVal1);
        map1.put(req2, reqVal2);
        map1.put(req3, reqVal3);

        Map<String, Object> map2 = new HashMap<>();
        map2.put(req1, reqVal1);
        map2.put(req2, reqVal2);
        map2.put(req3, reqVal3);
        map2.put(req4, reqVal4);

        Map<String, Object> map3 = new HashMap<>();
        map3.put(req1, reqVal1);
        map3.put(req2, reqVal2);
        map3.put(req3, reqVal3);
        map3.put(req4, reqVal4);
        map3.put(req5, reqVal5);

        Map<String, Object> map4 = new HashMap<>();
        map4.put(req1, reqVal1);
        map4.put(req2, reqVal2);
        map4.put(req3, reqVal3);
        map4.put(req4, reqVal4);
        map4.put(req5, reqVal5);
        map4.put(req6, reqVal6);

        for (int i = 0; i < batch; i++) {
            list.add(map1);
            list.add(map2);
            list.add(map3);
            list.add(map4);
        }
        return list;
    }

}
