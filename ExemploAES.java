/**
 * Criptografia com o algoritmo AES.
 * 
 * O objetivo é a criação de instâncias de algoritmos de criptografia
 * para criptografar um arquivo de texto.
 * 
 */
package crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class ExemploAES {
	
	private static final String arquivoCriptografado = "src/files/aes_arquivo_criptografado.txt";
	private static final String arquivoDescriptografado = "src/files/aes_arquivo_descriptografado.txt";
	
	// definindo o algoritmo de criptgrafia (simétrico) com AES
    private static final String ALGORITHM = "AES";
    
    // chave de criptografia (precisa estar segura!) 16 caracteres
    private byte[] key = "MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8);


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
	 * Criptografar o arquivo com o algoritmo AES.
	 * @param conteudo
	 * @return
	 * @throws Exception
	 */
	public byte[] criptografarArquivo(byte[] conteudo) throws Exception {
		
		// Inicia o objeto que guarda as chaves para o algoritmo
		SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
		
		// Objeto de cifra, com AES
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        
        // Inicia o processo
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Finaliza (doFinal é obrigatório)
        byte[] result = cipher.doFinal(conteudo);
		
        return result;
	}
	
	
	/**	
	 * 
	 * @param conteudo
	 * @return
	 * @throws Exception
	 */
	public byte[] descriptografarArquivo(byte[] conteudo) throws Exception {
		
		SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
		
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] result = cipher.doFinal(conteudo);
        
        return result;
        
	}
	
	/**
	 * Outros exemplos relevantes:
	 * 		http://commons.apache.org/proper/commons-crypto/xref-test/org/apache/commons/crypto/examples/CipherByteArrayExample.html
	 * 		http://commons.apache.org/proper/commons-crypto/xref-test/org/apache/commons/crypto/examples/CipherByteBufferExample.html
	 * 		https://msdn.microsoft.com/pt-br/library/te15te69(v=vs.110).aspx
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		ExemploAES exemplo =  new ExemploAES();
		
		System.out.println("\n -------------- \n Original \n --------------");
		byte[] textoOriginal = exemplo.carregarArquivo("src/files/aes_arquivo.txt");
		System.out.println(new String(textoOriginal, StandardCharsets.UTF_8));
		
		System.out.println("\n -------------- \n Criptografando \n --------------");
		byte[] textoCriptografado = exemplo.criptografarArquivo(textoOriginal);
		System.out.println(new String(textoCriptografado, StandardCharsets.UTF_8));
		exemplo.gravarArquivo(textoCriptografado, arquivoCriptografado);
		
		byte[] textoDescriptografado = exemplo.descriptografarArquivo(textoCriptografado);
		System.out.println("\n -------------- \n Descriptografando \n --------------");
		System.out.println(new String(textoDescriptografado, StandardCharsets.UTF_8));
		exemplo.gravarArquivo(textoDescriptografado, arquivoDescriptografado);
		
	}

}
