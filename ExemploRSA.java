package crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class ExemploRSA {

	private static final String msgCriptografada = "src/files/rsa_mensagem_criptografada.txt";
	private static final String msgDescriptografada = "src/files/rsa_mensagem_descriptografada.txt";
	
	/**
	 * Método de auxílio.
	 * Carrega o arquivo retorna seu conteúdo em bytes
	 * @param arquivo
	 */
	public byte[] carregarArquivo(String arquivo) {
		
		byte[] array = null;
		
		try {
			array = Files.readAllBytes(new File(arquivo).toPath());
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	/**
	 * Método de auxílio.
	 * Grava um array de bytes em um arquivo.
	 * @param conteudo
	 * @param caminho
	 * @throws Exception
	 */
	public void gravarArquivo(byte[] conteudo, String caminho) throws Exception {
		
		FileOutputStream stream = new FileOutputStream(caminho);
		try {
		    stream.write(conteudo);
		} finally {
		    stream.close();
		}
	}
	
	
	/**
	 * Gerando as chaves pública e privada
	 * Método RSA
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair geradorChaves() throws NoSuchAlgorithmException {
		
		// chaves de tamanho 2048 (padrão)
		final int lenChave = 2048;
		
		// criando uma instância do gerador de chaves com o RSA
		KeyPairGenerator gerador = KeyPairGenerator.getInstance("RSA");
		// inicializando com o tamanho de chave definido
		gerador.initialize(lenChave);
		
		// cria as chaves pública e privada
		return gerador.genKeyPair();
	}
	
	/**
	 * Método para criptografar a mensagem
	 * Exemplo utilizado: criptografia feita com a chave pública
	 * @param mensagem
	 * @param chavePublica
	 * @return
	 * @throws Exception
	 */
	public byte[] criptografarMensagem(byte[] mensagem, PublicKey chavePublica) throws Exception {
		
		Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, chavePublica);  

        return cipher.doFinal(mensagem);
	}
	
	/**
	 * Método para descriptografar uma mensagem
	 * Exemplo utilizado: descriptografia feita com chave privada
	 * @param mensagem
	 * @param chavePrivada
	 * @return
	 * @throws Exception
	 */
	public byte[] descriptografarMensagem(byte[] mensagem, PrivateKey chavePrivada) throws Exception{
		
		Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, chavePrivada);
        
        return cipher.doFinal(mensagem);
	}
	
	/**
	 * Apenas um teste de conceito.
	 * @param mensagem
	 * @param chavePublica
	 * @throws Exception
	 */
	public void testeFalha(byte[] mensagem, PublicKey chavePublica) throws Exception{
		
		// Mensagem = já foi criptografada com chave pública
		// Seria possíve descriptografar com a chave pública???
		
		Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, chavePublica);
        
        byte[] resultado = cipher.doFinal(mensagem);
        
        System.out.println("\n -------------- \n TESTE: Mensagem já criptografada \n --------------");
     	System.out.println(new String(mensagem, StandardCharsets.UTF_8));
     	
     	System.out.println("\n -------------- \n TESTE: Tentando descriptografar \n --------------");
     	System.out.println(new String(resultado, StandardCharsets.UTF_8));
     		
	}
	
	/**
	 * Execução
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		ExemploRSA exemplo = new ExemploRSA();
		
		KeyPair chaves = exemplo.geradorChaves();
		
		PublicKey chavePublica = chaves.getPublic();
		PrivateKey chavePrivada = chaves.getPrivate();
		
		//System.out.println("Chave pública: " + chavePublica);
		//System.out.println("Chave privada: " + chavePrivada);
		
		// Carregando a mensagem que deve ser mantida em segredo
		System.out.println("\n -------------- \n Mensagem \n --------------");
		byte[] mensagem = exemplo.carregarArquivo("src/files/rsa_mensagem.txt");
		System.out.println(new String(mensagem, StandardCharsets.UTF_8));
		
		// Criptografando a mensagem com a chave pública.
		// Somente o portador da chave privada conseguirá quebrar a mensagem
		System.out.println("\n -------------- \n Criptografando \n --------------");
		
		byte[] mensagemCriptografada = exemplo.criptografarMensagem(mensagem, chavePublica);
		System.out.println(new String(mensagemCriptografada));
		exemplo.gravarArquivo(mensagemCriptografada, msgCriptografada);
		
		// Descriptografando uma mensagem. Apenas com a chave privada é possível executar o processo!
		System.out.println("\n -------------- \n Descriptografando \n --------------");
		
		byte[] mensagemDescriptografada = exemplo.descriptografarMensagem(mensagemCriptografada, chavePrivada);
		System.out.println(new String(mensagemDescriptografada, StandardCharsets.UTF_8));
		exemplo.gravarArquivo(mensagemDescriptografada, msgDescriptografada);
		
		// Executem esse teste
		// Mensagem = já foi criptografada com chave pública
		// Seria possíve descriptografar com a chave pública???
		//exemplo.testeFalha(mensagemCriptografada, chavePublica);
		
		
	}

}
