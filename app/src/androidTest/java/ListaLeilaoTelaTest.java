import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.ui.activity.ListaLeilaoActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.alura.leilao.matchers.ViewMatcher.apareceLeilaoNaPosicao;

public class ListaLeilaoTelaTest  extends BaseTesteIntegracao {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activiy =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setup() throws IOException {
        limpaBasedeDadosDaApi();
    }

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregaImLeilaoNaApp() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("carro"));

        activiy.launchActivity(new Intent());
//        onView(allOf(withText("carro"), withId(R.id.item_leilao_descricao)))
//                .check(matches(isDisplayed()));
//
//        String formatoesperadocarro = new FormatadorDeMoeda().formata(0.00);

//        onView((allOf(withText(formatoesperadocarro), withId(R.id.item_leilao_maior_lance))))
//            .check(matches(isDisplayed()));

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(0, "carro", 0.00)));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregaLeilaoNaApp() throws IOException, InterruptedException {
        tentaSalvarLeilaoNaApi(new Leilao("carro"), new Leilao("casa"));

        activiy.launchActivity(new Intent());

//        onView(allOf(withText("carro"), withId(R.id.item_leilao_descricao)))
//                .check(matches(isDisplayed()));
//
//
//        String formatoesperadocarro = formatadorDeMoeda.formata(0.00);
//
//        onView((allOf(withText(formatoesperadocarro), withId(R.id.item_leilao_maior_lance))))
//                .check(matches(isDisplayed()));
//
//        onView(allOf(withText("casa"), withId(R.id.item_leilao_descricao)))
//                .check(matches(isDisplayed()));
//
//        String formatoesperadocasa = formatadorDeMoeda.formata(0.00);
//
//        onView((allOf(withText(formatoesperadocasa), withId(R.id.item_leilao_maior_lance))))
//                .check(matches(isDisplayed()));

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(0, "carro"
                        , 0.00)));
        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(1, "casa"
                        , 0.00)));
    }

    @Test
    public void deve_AparecerUltimatoLeilao_QuandoCarregaDeLeiloesDaApi () throws IOException {
        tentaSalvarLeilaoNaApi(
                new Leilao("carro"),
                new Leilao("Computador"),
                new Leilao("TV"),
                new Leilao("Notebook"),
                new Leilao("Console"),
                new Leilao("Jogos"),
                new Leilao("Estante"),
                new Leilao("Quadro"),
                new Leilao("Smartphone"),
                new Leilao("casa"));

        activiy.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(9))
                .check(matches(apareceLeilaoNaPosicao(9, "casa"
                        , 0.00)));
    }

}
