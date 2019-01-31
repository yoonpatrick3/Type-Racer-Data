package TypeRacer;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelData
{
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException
    {
    	
    	dataTracker.createData();
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
         
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("TypeRacer Data");
          
        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"WPM", "Accuracy", "Number of Words", "Average Word Length"});
        
        BufferedReader f = new BufferedReader(new FileReader("yoonData.txt"));
       
        int i = 2;
        for(String s = f.readLine(); s!=null; s = f.readLine()) {
        	String[] arr = s.split(" ");
        	
        	
        	
        	data.put(Integer.toString(i), new Object[] {arr[1], arr[2], arr[3],arr[4]});
      
        	i++;
        }
       
          
        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
               Cell cell = row.createCell(cellnum++);
               if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("yoonData.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("yoonData.xlsx written successfully on disk.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}