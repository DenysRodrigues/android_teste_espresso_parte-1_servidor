package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;
import br.com.alura.leilao.database.dao.UsuarioDAO;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class LancesLeilaoTelaTeste extends BaseTesteIntegracao{

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> mainActivity =
            new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setup() throws IOException {
        limpaBasedeDadosDaApi();
        limpaBancoDadosInterno();
    }


    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberUmLance() throws IOException {
//        Salvar leilão na API
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

//          Inicializar a main Activity
        mainActivity.launchActivity(new Intent());

//            Clica no leilao
        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

//            Clica no fab da tela de lances do leilao
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona),
                isDisplayed()))
                .perform(click());

//            Verifica se aparece dialog de aviso por não ter usuário com título e mensagem esperada
        onView(allOf(withText("Usuários não encontrados"),
                withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));

        onView(allOf(withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."),
                withId(android.R.id.message)))
                .check(matches(isDisplayed()));

//            Clica no botão "Cadastrar Usuário"
        onView(allOf(withText("Cadastrar usuário"),
                isDisplayed()))
                .perform(click());

//            Clica no EditText e preenche com o nome do usuário
        onView(allOf(withId(R.id.lista_usuario_fab_adiciona),
                isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.form_usuario_nome_edittext),
                isDisplayed()))
                .perform(click(),
                        typeText("Denys"),
                        closeSoftKeyboard());

//            Clica em Adicionar
        onView(allOf(withId(android.R.id.button1), withText("Adicionar"),
                isDisplayed()))
                .perform(scrollTo(), click());

//            Clica no back do Android
        pressBack();

//           Clica no fab lances do leilao
//        onView(allOf(withId(R.id.lances_leilao_fab_adiciona),
//                isDisplayed()))
//                .perform(click());

//            Verifica visibilidade do dialog com otítulo esperado
        propoeNovoLance("200", 1, "Denys");

//        Fazer assertion para as views de maior e menor lance, e também, para os maiores lances
        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatador.formata(200)),
                isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance))
                .check(matches(allOf(withText(formatador.formata(200)),
                isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatador.formata(200.0) + " - (1) Denys\n"),
                isDisplayed())));
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberTresLances () throws IOException {
//            Salvar leilão na API
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        tentaSalvarUsuariosNoBancoDeDados(new Usuario("Denys"), new Usuario("Karina"));

//            Inicializar a main Activity
        mainActivity.launchActivity(new Intent());

//            Clica no leilao
        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


//            Verifica visibilidade do dialog com otítulo esperado
        propoeNovoLance("200", 1, "Denys");

        propoeNovoLance("300", 2, "Karina");

        propoeNovoLance("400", 1, "Denys");


//        Fazer assertion para as views de maior e menor lance, e também, para os maiores lances
        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatador.formata(400)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance))
                .check(matches(allOf(withText(formatador.formata(200)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatador.formata(400.0 ) + " - (1) Denys\n" +
                                formatador.formata(300.0) + " - (2) Karina\n" +
                                formatador.formata(200.0) + " - (1) Denys\n"),
                        isDisplayed())));
    }

    @Test
    public void deve_AtualizarLanceDoLeilao_QuandoReceberUmLanceMuitoAlto () throws IOException{
//            Salvar leilão na API
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        tentaSalvarUsuariosNoBancoDeDados(new Usuario("Denys"));

//            Inicializar a main Activity
        mainActivity.launchActivity(new Intent());

//            Clica no leilao
        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


//            Verifica visibilidade do dialog com otítulo esperado
        propoeNovoLance("2000000000", 1, "Denys");


//        Fazer assertion para as views de maior e menor lance, e também, para os maiores lances
        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatador.formata(2000000000)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance))
                .check(matches(allOf(withText(formatador.formata(2000000000)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatador.formata(2000000000) + " - (1) Denys\n"),
                        isDisplayed())));
    }

    private void propoeNovoLance(String valorLance, int idUsuario, String nomeUsuario) {
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona),
                isDisplayed()))
                .perform(click());

        onView(allOf(withText("Novo lance"),
                withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));


        onView(allOf(withId(R.id.form_lance_valor_edittext),
                isDisplayed()))
                .perform(click(),
                        typeText(valorLance),
                        closeSoftKeyboard());


        onView(allOf(withId(R.id.form_lance_usuario),
                isDisplayed()))
                .perform(click());

        onData(is(new Usuario(idUsuario, nomeUsuario)))
                .inRoot(isPlatformPopup())
                .perform(click());


        onView(allOf(withText("Propor"),
                isDisplayed()))
                .perform(click());
    }

    private void tentaSalvarUsuariosNoBancoDeDados(Usuario... usuarios) {
        UsuarioDAO dao = new UsuarioDAO(InstrumentationRegistry.getTargetContext());

        for (Usuario usuario :
                    usuarios) {
            Usuario usuarioSalvo = dao.salva(usuario);

         if (usuarioSalvo == null){
                Assert.fail("Usuário " + usuario + " não foi salvo");
            }

        }
    }

//    @After
//    public void tearDown() throws IOException {
//        limpaBasedeDadosDaApi();
//        limpaBancoDadosInterno();
//    }


}
