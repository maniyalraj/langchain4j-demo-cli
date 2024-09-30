package demo.lunaconf.rag

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.ollama.OllamaEmbeddingModel
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore

class DocumentRetriever {

  def getContentRetriever(documentPath: String): EmbeddingStoreContentRetriever ={

    val documentParser = new TextDocumentParser()
    val document = loadDocument(documentPath, documentParser)

    val documentSplitter = DocumentSplitters.recursive(300,0)
    val segments = documentSplitter.split(document)

    val embeddingModel = OllamaEmbeddingModel.builder()
      .baseUrl("http://localhost:11434")
      .modelName("all-minilm")
      .build()

    val embeddings = embeddingModel.embedAll(segments).content()

    val embeddingStore = new InMemoryEmbeddingStore[TextSegment]()
    embeddingStore.addAll(embeddings, segments)

    val contentRetriever = EmbeddingStoreContentRetriever.builder()
      .embeddingStore(embeddingStore)
      .embeddingModel(embeddingModel)
      .maxResults(20)
      .minScore(0.5)
      .build()

    contentRetriever

  }


}
