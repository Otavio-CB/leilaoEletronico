package br.gov.sp.fatec.lp2.utils;

import br.gov.sp.fatec.lp2.entity.dto.LeilaoDETDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class ExportUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static void exportLeilaoExportacaoToDetFile(LeilaoDETDTO leilaoDETDTO, String filePath) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, leilaoDETDTO);
    }
}
