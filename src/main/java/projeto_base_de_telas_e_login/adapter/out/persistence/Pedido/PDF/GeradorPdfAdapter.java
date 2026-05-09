package projeto_base_de_telas_e_login.adapter.out.persistence.Pedido.PDF;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.adapter.out.persistence.Pedido.PDF.PdfTemplatePedido.PdfPedidoTemplate;
import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;
import projeto_base_de_telas_e_login.domain.repository.GeradorPdfPort;

import java.io.ByteArrayOutputStream;


@Component
public class GeradorPdfAdapter implements GeradorPdfPort {

    @Override
    public byte[] gerarPdfPedido(Pedido pedido) {

        if (pedido.getId() == null) {
            throw new IllegalStateException("Pedido precisa estar salvo antes de gerar PDF");
        }

        if (pedido.getFormaPagamento() == null) {
            throw new IllegalStateException("Forma de pagamento não pode ser null");
        }

        String html = PdfPedidoTemplate.gerarHtml(pedido);

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