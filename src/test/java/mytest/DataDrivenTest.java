package mytest;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Iterator;

public class DataDrivenTest {

    @DataProvider(name = "scenarioProvider")
    public Iterator<Object[]> scenarioProvider() {
        return new ScenarioProvider();
    }

    @Test(dataProvider = "scenarioProvider")
    public void runScenario(Scenario scenario) {
        scenario.run();
    }
    @DataProvider(name = "excel-data")
    public Object[][] excelDataProvider() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("path/to/excel.xlsx"); // Excel 文件路径
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
        int rowCount = sheet.getLastRowNum();
        int colCount = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
            }
        }

        workbook.close();
        fileInputStream.close();
        return data;
    }

    @Test(dataProvider = "excel-data")
    public void testMethod(String username, String password) {
        // 使用数据进行测试
        System.out.println("Testing with username: " + username + " and password: " + password);
        // 在这里添加你的 Selenium 代码
    }
}

