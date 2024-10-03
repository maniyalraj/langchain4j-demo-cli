package demo.lunaconf.rag

import java.util

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.DocumentSplitter
import dev.langchain4j.data.document.loader.UrlDocumentLoader
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.ollama.OllamaEmbeddingModel
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore

class DocumentRetriever {

  def getContentRetriever(url: String): EmbeddingStoreContentRetriever = {

    val document: Document = UrlDocumentLoader.load(url, new ApacheTikaDocumentParser())

    val documentSplitter: DocumentSplitter = DocumentSplitters.recursive(200, 0)
    val segments: util.List[TextSegment] = documentSplitter.split(document)

    val embeddingModel: OllamaEmbeddingModel = OllamaEmbeddingModel
      .builder()
      .baseUrl("http://localhost:11434")
      .modelName("nomic-embed-text")
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
