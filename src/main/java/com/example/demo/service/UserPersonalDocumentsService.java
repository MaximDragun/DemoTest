package com.example.demo.service;

import com.example.demo.entity.UserPersonalDocuments;
import com.example.demo.entity.UserDocument;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPersonalDocumentsService {
    private final UserPersonalDocumentsDB userPersonalDocumentsDB;
    @Value("${app.document.path.applicationforbrokerageservice}")
    private Resource stateFile;

    public void create(UUID uuid) throws IOException {

        try (InputStream inputStream = stateFile.getInputStream()) {

            XWPFDocument document = new XWPFDocument(inputStream);
            one(document);//формируем XWPFDocument с замененными плейсхолдерами
            PdfOptions options = PdfOptions.create();
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(document, pdfOutputStream, options);// конвертируем в pdf
            document.close();

            List<UserDocument> userDocuments = new ArrayList<>();
            UserDocument max = new UserDocument("Max", new Binary(pdfOutputStream.toByteArray()));
            userDocuments.add(max);
            userPersonalDocumentsDB.createEnt(UserPersonalDocuments.builder()//сохраняем объект в базу
                    .userId(uuid)
                    .userDocuments(userDocuments)
                    .created(LocalDateTime.now())
                    .build());
        }
    }

    public byte[] getDoc(String userId) {// получаем массив байт pdf файла из монги
        Optional<UserPersonalDocuments> ent = userPersonalDocumentsDB.getEnt(UUID.fromString(userId));
        return ent.map(userPersonalDocuments -> userPersonalDocuments.getUserDocuments().get(0).getDocument()).get().getData();
    }

    public void one(XWPFDocument document) { //формируем мапу из плейсхолдеров
        DateTimeFormatter ru = DateTimeFormatter.ofPattern("d MMMM yyyy 'года'", new Locale("ru"));
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("brokeraccountname", "Max");//ЗНБО,
        placeholderMap.put("depoaccountname", "Max");//ДД,
        placeholderMap.put("localdate", ru.format(LocalDate.now()));//СНОПД,ЗНБО,ДД,
        placeholderMap.put("customerlastname", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("customerfirstname", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("customermiddlename", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("customerbirthdate", "Max");//АФЛ
        placeholderMap.put("passportseries", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("passportissuedby", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("passportdepartamentcode", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("passportdateofissue", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("passportnumber", "Max");//СНОПД,ЗНБО,ДД,АФЛ
        placeholderMap.put("addressregion", "Max");//СНОПД,ДД,АФЛ
        placeholderMap.put("addresslocation", "Max");//СНОПД,ДД,АФЛ
        placeholderMap.put("addressstreet", "Max");//СНОПД,ДД,АФЛ
        placeholderMap.put("addresshousenumber", "Max");//СНОПД,ДД,АФЛ
        placeholderMap.put("addressapartmentnumber", "Max");//СНОПД,ДД,АФЛ
        placeholderMap.put("credentialsinn", "Max");//АФЛ
        placeholderMap.put("customermobilephone", "Max");//АФЛ
        placeholderMap.put("customeremail", "Max");//АФЛ
        placeholderMap.put("credentialsresidence", "Max");//АФЛ
        placeholderMap.put("credentialsabroadtax", "Max");//АФЛ
        placeholderMap.put("credentialsbeneficialowner", "Max");//АФЛ
        placeholderMap.put("credentialsrepresentative", "Max");//АФЛ
        placeholderMap.put("credentialsbeneficiary", "Max");//АФЛ

        replacePlaceholders(document, placeholderMap);

        System.out.println("Итоговый документ успешно создан.");

    }

    public void replacePlaceholders(XWPFDocument doc, Map<String, String> placeholderMap) { // метод для замены плейсхолдеров в docx файле
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && placeholderMap.containsKey(text)) {
                    run.setText(placeholderMap.get(text), 0);
                    run.setColor("000000");
                }
            }
        }
    }
}
