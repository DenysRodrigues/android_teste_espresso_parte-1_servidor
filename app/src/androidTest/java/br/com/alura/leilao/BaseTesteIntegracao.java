package br.com.alura.leilao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;

import java.io.IOException;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.api.retrofit.client.TesteWebClient;

public abstract class BaseTesteIntegracao {
    private final TesteWebClient WebClient = new TesteWebClient();

    private static final String ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API = "Banco de dados não foi limpo";
    private static final String LEILAO_NÂO_FOI_SALVO = "Leilão não foi salvo:";


    protected void limpaBasedeDadosDaApi() throws IOException {
        boolean bancodeDadosFoiLimpo = !WebClient.limpaBancodeDados();
        if (bancodeDadosFoiLimpo) {
            Assert.fail(ERRO_FALHA_LIMPEZA_DE_DADOS_DA_API);
        }
    }

    protected void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes){
            Leilao leilaoSalvo = WebClient.salva(leilao);

            if(leilaoSalvo == null) Assert.fail(LEILAO_NÂO_FOI_SALVO + leilao.getDescricao());
        }
    }

    protected void limpaBancoDadosInterno() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(BuildConfig.BANCO_DE_DADOS);
    }
}
