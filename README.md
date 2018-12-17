# PlayNews
## O PlayNews consiste em um app para dispositivos Android para ouvir notícias através de feeds RSS.

Visando os ambientes de trânsito, academias e trabalho, onde por muitas vezes não é possível manter-se informado através da leitura, o PlayNews vem para solucionar esse problema, possuindo uma interface intuitiva,com poucos toques é possível adicionar seus  portais de notícias favoritos e se manter informado através da ferramenta de narração.

## Descrição técnica:
### Overview
A Activity principal possui um TabLayout onde é possível navegar pelo Feed e pelos portais assinados separados em duas abas, as duas abas apresentam seus conteúdos através de RecyclerViews, que possuem imagens e textos. As notícias podem ser atualizadas através de um botão de refresh na ActionBar, e através da mesma é possivel também adicionar novos feeds. O conteúdo da notícia é apresentado em uma ReadActivity contendo a imagem e a notícia completa, através de um botão de play o usuário é capaz de ouvir a notícia.

### Requisições
As requisições http são feitas utilizando a classe HttpURLConnection da biblioteca nativa. Após feita a requisição, utilizando a biblioteca XmlPullParser são extraídas as informações relevantes do feed, em seguida as mesmas são persistidas em um banco de dados local através da biblioteca Room. 
  
### Apresentação
  Ao fim de uma requisição as notícias populam as RecyclerViews. Com objetivo de melhorar a experiência do usuário, as imagens das notícias são baixadas apenas após a conclusão da requisição do feed, e são atualizadas na recyclerView a medida que vão sendo baixadas, isso faz com que o usuário tenha um feedback imediato das notícias, mesmo que sem as imagens, que em geral possuem alta resolução e por isso levam tempo para serem baixadas e redimensionadas. Foi utilizada a biblioteca Picasso para renderização e redimensionamento da imagem na apresentação da notícia.
  
### Gerenciamento
  As imagens são baixadas e salvas na memória interna ao mesmo tempo em que são inseridas em uma cache, isso é feito pois o download das imagens é efetuado em ordem de apresentação nas RecyclerViews, favorecendo os cache_hits nas primeiras notícias. O processo de carregamento de uma imagem em uma view consiste primeiro na verificação da cache, caso não encontrada a imagem a mesma é buscada na memória interna e salva na cache, agindo dessa forma é possível ter uma maior fluidez nas listas mesmo com várias imagens sendo exibidas.
  
### Dados
  Os dados são persistidos na memória interna através da biblioteca Room que fornece uma abstração de um banco de dados SQL para a aplicação, por motivos de tempo a chave primária escolhida para uma noticia foi seu título, essa chave serve tanto para identificar as imagens da notícia na cache/memória, quanto para evitar downloads repetido.

### Text-to-speech
  Foi utilizada a biblioteca nativa do Android para o tts, dessa forma é possível ouvir as notícias mesmo sem conexão com a internet e ainda evitar a necessidade de utilização de API's de terceiros que cobram taxas pelo serviço. Para a obtenção da notícia completa foi utilizada a biblioteca jsoup para fazer o web-scrapping da tag correta do Html.
  
 ### Principais bibliotecas utilizadas
 
 Room
 Picasso
 Jsoup
 XmlPullParser

