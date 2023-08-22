package com.bonestew.popmate.helper;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Snippet;

public class RestDocsHelper {

    public static RestDocumentationResultHandler customDocument(Snippet... snippets) {
        return document(
            "{class-name}/{method-name}",
            preprocessRequest(Preprocessors.prettyPrint()),
            preprocessResponse(Preprocessors.prettyPrint()),
            snippets
        );
    }
}
