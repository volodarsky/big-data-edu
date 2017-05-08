package edu.bigdata.elasticsearch;

import lombok.SneakyThrows;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.xml.bind.JAXBContext;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by vromanov on 07.05.17.
 */
public class ElasticSearchLoader {


    public static final String PRODUCTS_ACTIVE_XML_ZIP = "20151101productsActive.xml.zip";
    public static final String PRODUCTS_SAMPLE_XML = "products_0001_43900_to_1063518.xml";


    @SneakyThrows
    public static void main(String[] args) {
        //load();
        try (TransportClient client = buildTransportClient();
             BulkProcessor bulkProcessor = buildBulkProcessor(client);
             InputStream fileInputStream = fromClasspath(PRODUCTS_SAMPLE_XML).openStream()) {

            processFile(bulkProcessor, fileInputStream);
        }
    }

    @SneakyThrows
    public static void load() {

        try (TransportClient client = buildTransportClient();
             BulkProcessor bulkProcessor = buildBulkProcessor(client);
             ZipFile zipFile = loadZipFile(PRODUCTS_ACTIVE_XML_ZIP)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                System.out.println(zipEntry.getName());

                processFile(bulkProcessor, zipFile.getInputStream(zipEntry));
            }
        }
    }

    private static void processFile(BulkProcessor bulkProcessor, InputStream fileInputStream) {
        Products records = parse(fileInputStream);

        for (Product record : records.productList) {
            System.out.println(record);
            IndexRequest products =
                    new IndexRequest("products","product")
                            .id(record.productId)
                            .source(record.toMap());

            bulkProcessor.add(products);
        }
    }

    @SneakyThrows
    private static ZipFile loadZipFile(String zipFilePath) {
        return new ZipFile(new File(fromClasspath(zipFilePath).toURI()));
    }

    private static BulkProcessor buildBulkProcessor(TransportClient client) {
        //client.prepareIndex("products", "product").get();
        return BulkProcessor.builder(client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                        System.out.println("beforeBulk: " + executionId);
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        System.out.println("afterBulk: " + executionId);
                        System.out.println(response);
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        System.out.println("afterBulk: " + executionId);
                        failure.printStackTrace();
                    }
                })
                .setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    @SneakyThrows
    private static TransportClient buildTransportClient() {
        return new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }


    @SneakyThrows
    public static Products parse(InputStream products) {
        return (Products) JAXBContext.newInstance(
                Products.class).createUnmarshaller().unmarshal(products);
    }

    private static URL fromClasspath(String zipFileName) {
        return ElasticSearchLoader.class.getClassLoader().getResource(zipFileName);
    }

    @SneakyThrows
    private static void fromUrl() {
        URL zipFile = new URL("https://drive.google.com/open?id=0Bx8vMZ0U_cKLdkkxUWVBVkZDLXM");
        HttpURLConnection connection = (HttpURLConnection) zipFile.openConnection();
        connection.setRequestMethod("GET");


        ZipInputStream zipInputStream = new ZipInputStream(
                new BufferedInputStream(connection.getInputStream()));
        while (zipInputStream.available() > 0) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            System.out.println(nextEntry.getName());
        }
    }
}
