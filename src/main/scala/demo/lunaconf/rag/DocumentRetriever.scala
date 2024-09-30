package demo.lunaconf.rag

import java.util

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.DocumentSplitter
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.ollama.OllamaEmbeddingModel
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore

class DocumentRetriever {

  def getContentRetriever(documentPath: String): EmbeddingStoreContentRetriever = {

    val documentParser: TextDocumentParser = new TextDocumentParser()
    val document: Document = loadDocument(documentPath, documentParser)

    val documentSplitter: DocumentSplitter = DocumentSplitters.recursive(300, 0)
    val segments: util.List[TextSegment] = documentSplitter.split(document)

    val embeddingModel: OllamaEmbeddingModel = OllamaEmbeddingModel
      .builder()
      .baseUrl("http://localhost:11434")
      .modelName("all-minilm")
      .build()

    val embeddings: util.List[Embedding] = embeddingModel.embedAll(segments).content()

    val embeddingStore: InMemoryEmbeddingStore[TextSegment] =
      new InMemoryEmbeddingStore[TextSegment]()
    embeddingStore.addAll(embeddings, segments)

    EmbeddingStoreContentRetriever
      .builder()
      .embeddingStore(embeddingStore)
      .embeddingModel(embeddingModel)
      .maxResults(20)
      .minScore(0.5)
      .build()
  }
}
