package ifpe.ts.unidade04.avaliacao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.github.bonigarcia.wdm.WebDriverManager;

class TesteCalculadora {

	private static ChromeDriver driver;

	private static NodeList procedimentos;

	private static NodeList casosExponenciacao;
	private static NodeList casosRadiciacao;
	private static NodeList casosJurosCompostos;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		procedimentos = getProcedimentosDoXml("C:\\Users\\pablo\\disciplinas-ifpe\\ts\\unidade4\\avaliacao\\src\\test\\resources\\casos_teste.xml");

		Element procedimentoExponenciacao = (Element) procedimentos.item(0);
		casosExponenciacao = procedimentoExponenciacao.getElementsByTagName("caso");

		Element procedimentoRadiciacao = (Element) procedimentos.item(1);
		casosRadiciacao = procedimentoRadiciacao.getElementsByTagName("caso");

		Element procedimentosJurosCompostos = (Element) procedimentos.item(2);
		casosJurosCompostos = procedimentosJurosCompostos.getElementsByTagName("caso");

	}

	@BeforeEach
	void setUp() throws Exception {
		driver.get("http://www.calculadoraonline.com.br/basica");
	}

	@AfterAll
	static void tearDown() throws Exception {
		driver.quit();
	}

	@ParameterizedTest
	@ValueSource(strings = { "0", "1", "2", "3", "4" })
	void testaRadiciacao(int codCaso) {
		Element caso = (Element) casosRadiciacao.item(codCaso);
		String radicando = caso.getElementsByTagName("radicando").item(0).getTextContent();
		String indice = caso.getElementsByTagName("indice").item(0).getTextContent();
		String resultadoEsperado = caso.getElementsByTagName("resultadoEsperado").item(0).getTextContent();
		String resultado = inserirValoresDaRadiciacaoNaCalculadora(radicando, indice);
		assertEquals(resultadoEsperado, resultado);
	}

	@ParameterizedTest
	@ValueSource(strings = { "0", "1", "2", "3", "4" })
	void testaExponenciacao(int codCaso) {
		Element caso = (Element) casosExponenciacao.item(codCaso);
		String base = caso.getElementsByTagName("base").item(0).getTextContent();
		String expoente = caso.getElementsByTagName("expoente").item(0).getTextContent();
		String resultadoEsperado = caso.getElementsByTagName("resultadoEsperado").item(0).getTextContent();
		String resultado = inserirValoresDaExponenciacaoNaCalculadora(base, expoente);
		assertEquals(resultadoEsperado, resultado);
	}

	@ParameterizedTest
	@ValueSource(strings = { "0", "1", "2", "3", "4" })
	void testaJurosCompostos(int codCaso) {
		Element caso = (Element) casosJurosCompostos.item(codCaso);
		String capital = caso.getElementsByTagName("capital").item(0).getTextContent();
		String taxa = caso.getElementsByTagName("taxa").item(0).getTextContent();
		String numPeriodos = caso.getElementsByTagName("numPeriodos").item(0).getTextContent();
		String resultadoEsperado = caso.getElementsByTagName("resultadoEsperado").item(0).getTextContent();
		String resultado = inserirValoresDoJurosCompostosNaCalculadora(capital, taxa, numPeriodos);
		assertEquals(resultadoEsperado, resultado);
	}

	String inserirValoresDaRadiciacaoNaCalculadora(String radicando, String indice) {
		driver.findElement(By.id("b23")).click();
		driver.findElement(By.id("cx23_0")).sendKeys("" + radicando);
		driver.findElement(By.id("cx23_1")).sendKeys("" + indice);
		driver.findElement(By.cssSelector("button.uk-button.uk-button-default")).click();
		String resposta = driver.findElement(By.id("TIExp")).getAttribute("value");
		return resposta;
	}

	String inserirValoresDaExponenciacaoNaCalculadora(String base, String expoente) {
		driver.findElement(By.id("b31")).click();
		driver.findElement(By.id("cx31_0")).sendKeys("" + base);
		driver.findElement(By.id("cx31_1")).sendKeys("" + expoente);
		driver.findElement(By.cssSelector("button.uk-button.uk-button-default")).click();
		String resposta = driver.findElement(By.id("TIExp")).getAttribute("value");
		return resposta;
	}

	String inserirValoresDoJurosCompostosNaCalculadora(String capital, String taxa, String numPeridos) {
		driver.findElement(By.id("b58")).click();
		driver.findElement(By.id("cx58_0")).sendKeys("" + capital);
		driver.findElement(By.id("cx58_1")).sendKeys("" + taxa);
		driver.findElement(By.id("cx58_2")).sendKeys("" + numPeridos);
		;
		driver.findElement(By.cssSelector("button.uk-button.uk-button-default")).click();
		String resposta = driver.findElement(By.id("TIExp")).getAttribute("value");
		return resposta;
	}

	private static NodeList getProcedimentosDoXml(String caminhoArquivo)
			throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File(caminhoArquivo);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		Element dados = (Element) doc.getElementsByTagName("suiteDeTeste").item(0);
		NodeList listaProcedimentos = dados.getElementsByTagName("procedimento");
		return listaProcedimentos;
	}

}