package Asclepio.Pedido.Pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import Asclepio.Pedido.Pedido;

import java.io.ByteArrayOutputStream;

@Service
public class PedidoPdfService {

    public byte[] gerarPdf(Pedido pedido) {

        String html = PedidoPdfTemplate.gerarHtml(pedido);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do pedido", e);
        }
    }
}