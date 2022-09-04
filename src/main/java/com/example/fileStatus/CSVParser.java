package com.example.fileStatus;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class CSVParser {
    
    public List<FileDetails> getFileDetails() throws FileNotFoundException
    {

    
        CSVReader csvReader = null;

        String fileName = "src/main/resources/input.csv";
        csvReader = new CSVReader(new FileReader(fileName));

        CsvToBean csvToBean = new CsvToBeanBuilder<>(csvReader)
            .withType(FileDetails.class)
            .withIgnoreEmptyLine(true)
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        List<FileDetails> list = csvToBean.parse();

        List<FileDetails> finallist = list.stream().filter(value -> StringUtils.isNoneEmpty(value.getAccount_number())).collect(Collectors.toList());

        return finallist;
    }
}
