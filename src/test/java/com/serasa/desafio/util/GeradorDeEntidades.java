package com.serasa.desafio.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeradorDeEntidades {

    public static Object gerarObjetoPeloNomeDoJson(String jsonNome, Class classeNome) throws IOException {
        Path arquivoEmJson = Path.of("src/test/resources/" + jsonNome + ".json");
        return jsonParaObjeto(Files.readString(arquivoEmJson), classeNome);
    }

    public static Object gerarObjetoPeloNomeDoJsonLista(String jsonNome, TypeToken TypeToken) throws IOException {
        Path arquivoEmJson = Path.of("src/test/resources/" + jsonNome + ".json");
        Gson gson = new Gson();
        return gson.fromJson(Files.readString(arquivoEmJson), TypeToken.getType());
    }


    public static Object jsonParaObjeto(String jsonEmString, Class classeNome) {
        Gson gson = new Gson();
        return gson.fromJson(jsonEmString, classeNome);
    }
}
