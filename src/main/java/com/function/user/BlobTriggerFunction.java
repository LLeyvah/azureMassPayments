package com.function.user;
import com.function.user.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.*;
import java.net.URISyntaxException;
import com.microsoft.azure.functions.*;

public class BlobTriggerFunction {

    private String containerName = "container-out";
    private String storageConn = "DefaultEndpointsProtocol=https;AccountName=stamasspyaments;AccountKey=YWL7M/vXNjdb5cpzgIH7VvtZOyppXsSAmDEv+LbceWksGNyLielLzh2vUN+72ctM+fwvhEQbb5L/4cViXbqDvA==;EndpointSuffix=core.windows.net";
    private static String pathx = "c:\\Azure\\demo.txt";

    CloudBlobContainer container = null;
    CloudBlobClient blobClient = null;
    CloudStorageAccount storageAccount;

    @FunctionName("BlobTriggerFunction")
    @StorageAccount("ConnectionFunction")
    public void run(@BlobTrigger(name = "content", path = "javafunction/{name}", dataType = "binary") byte[] content,
            @BindingName("name") String name, final ExecutionContext context)
            throws URISyntaxException, StorageException, IOException {

       
        try {

            storageAccount = CloudStorageAccount.parse(storageConn);
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(containerName);
            byte[] bytes = content;
            File file = new File(pathx);
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            printContent(file);
            CloudBlockBlob blob = container.getBlockBlobReference(file.getName());
            blob.uploadFromFile(pathx);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }  
  public static void printContent(File file) throws Exception {
        Map<HeaderColumn, List<String>> columns = new LinkedHashMap<>();
        Map<DetailColumn, List<String>> columns1 = new LinkedHashMap<>();   
        Map<DocumentColumn, List<String>> columns2 = new LinkedHashMap<>();      
        for (HeaderColumn c : HeaderColumn.VALUES) {
            columns.put(c, new ArrayList<>());
        }
        for (DetailColumn c : DetailColumn.VALUES) {
            columns1.put(c, new ArrayList<>());
        }
        for (DocumentColumn c : DocumentColumn.VALUES) {
            columns2.put(c, new ArrayList<>());
        }
        String csvFile = pathx;        
        String line = "";
        // create BufferedReader to read csv file
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        br.readLine();
        
        // Read the file line by line starting from the second line
        while ((line = br.readLine()) != null) {
            // Get all tokens available in line
            
            String[] tokens = line.split(",");
            if (tokens.length > 0) {
                for (int i = 0; i < tokens.length; i++) {
                    if(tokens[i].trim().charAt(i)=='2'){
                        columns.get(DetailColumn.VALUES[i]).add(tokens[i].trim());
                     //   columns.get(DetailColumn.VALUES[i++]).add(tokens[i].trim().substring(2,1));
                    }else if(tokens[i].trim().charAt(i)=='3'){    
                        columns1.get(DocumentColumn.VALUES[i]).add(tokens[i].trim());
                    }else{
                        columns2.get(HeaderColumn.VALUES[i]).add(tokens[i].trim());
                    }
                }
            }
         //   TypeRegister,QuantityAmount,DateProcess,TypeAccount,AccountCurrency,NumberAccount;
    }

}
}

