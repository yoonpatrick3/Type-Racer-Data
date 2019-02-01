package TypeRacer;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
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
    	//Checks time
    	long startTime = System.currentTimeMillis();
    	
    	
    	//Makes textfile of TypeRacer data
    	System.out.println("\fWhich account should I check?");
    	Scanner in = new Scanner(System.in);
		String username = in.nextLine();
		
	
		
    	dataTracker.createData(username);
    	
    	System.out.println("Now making the excel file!");
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
         
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("TypeRacer Data");
          
        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"WPM", "Accuracy", "Number of Words", "Average Word Length"});
        
    	String textFile = username + "DATAFILE.txt";
        BufferedReader f = new BufferedReader(new FileReader(textFile));
       
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
        	String xlsxFile = username + "DATAFILE.xlsx";
            FileOutputStream out = new FileOutputStream(new File(xlsxFile));
            workbook.write(out);
            out.close();
            long endTime = System.currentTimeMillis();
            System.out.println(xlsxFile + " written successfully on disk.");
            System.out.println("This took " + (endTime-startTime)/1000 + " seconds");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}