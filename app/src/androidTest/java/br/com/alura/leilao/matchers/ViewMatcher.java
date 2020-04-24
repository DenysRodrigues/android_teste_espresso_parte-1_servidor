package br.com.alura.leilao.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.alura.leilao.R;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class ViewMatcher {

    public static Matcher<? super View> apareceLeilaoNaPosicao(final int posicaoEsperada,
                                                               final String descricaoEsperada,
                                                               final double maiorLanceEsperado) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            private final Matcher<View> displayed = isDisplayed();
            private final FormatadorDeMoeda formatador = new FormatadorDeMoeda();

            @Override
            public void describeTo(Description description) {
                description.appendText("View com descrição ").appendValue(descricaoEsperada)
                        .appendText(", maior lance ").appendValue(formatador.formata(maiorLanceEsperado))
                        .appendText(" na posição ").appendValue(posicaoEsperada)
                        .appendText(" não foi encontrada ");

                description.appendDescriptionOf(displayed);
            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                RecyclerView.ViewHolder viewHolderDevolvido = item.findViewHolderForAdapterPosition(posicaoEsperada);

                if (viewHolderDevolvido == null) {
                    throw new IndexOutOfBoundsException("View do ViewHolder na posição " + posicaoEsperada + " não foi encontrada");
                }

                View viewDoHolder = viewHolderDevolvido.itemView;
                boolean temDescricaoEsperada = apareceDescricaoEsperada(viewDoHolder);


                boolean temMaiorLanceEsperado = apareceMaiorLanceEsperado(viewDoHolder);


                return temDescricaoEsperada &&
                        temMaiorLanceEsperado &&
                        displayed.matches(viewDoHolder);

            }

            private boolean apareceMaiorLanceEsperado(View viewDoHolder) {
                TextView textViewMarioLance = viewDoHolder.findViewById(R.id.item_leilao_maior_lance);
                return textViewMarioLance.getText().toString()
                        .equals(formatador.formata(maiorLanceEsperado)) &&
                        displayed.matches(textViewMarioLance);
            }

            private boolean apareceDescricaoEsperada(View viewDoHolder) {
                TextView textViewDescricao = viewDoHolder.findViewById(R.id.item_leilao_descricao);
                return textViewDescricao.getText()
                        .toString().equals(descricaoEsperada) &&
                        displayed.matches(textViewDescricao);
            }
        };
    }
}
