package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExemploHash {

	
	
	/**
	 * Método para gerar valores de HASH para string/texto
	 * @param mensagem
	 * @param algoritmo
	 * @return
	 * @throws Exception
	 */
	public byte[] gerarHashString(byte[] mensagem, String algoritmo) throws Exception {
		
		MessageDigest digest = MessageDigest.getInstance(algoritmo);
		
        byte[] resultado = digest.digest(mensagem);
        
        return resultado;
	}
	
	/**
	 * Método auxiliar de conversão de array de bytes para uma string hexadecimal
	 * @param bytes
	 * @return
	 */
	public String byteToString(byte[] bytes) {
        StringBuffer resultado = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            resultado.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return resultado.toString();
    }
	
	/**
	 * Método para gerar valores de HASH para arquivos binários
	 * Faz a leitura em lotes de bytes e gera o hash em cada um até completar 
	 * toda a iteração do arquivo
	 * @param file
	 * @param algoritmo
	 * @return
	 * @throws Exception
	 */
	public String gerarHashFile(File file, String algoritmo) throws Exception {
		
	    try (FileInputStream inputStream = new FileInputStream(file)) {
	    	
	        MessageDigest digest = MessageDigest.getInstance(algoritmo);
	 
	        byte[] bytes = new byte[1024];
	        int bytesRead = -1;
	 
	        while ((bytesRead = inputStream.read(bytes)) != -1) {
	            digest.update(bytes, 0, bytesRead);
	        }
	        byte[] resultado = digest.digest();
	 
	        return byteToString(resultado);
	        
	    } catch (Exception ex) {
	        throw new Exception("Erro", ex);
	    }
	}
	
	
	/**
	 * Aplicação 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		ExemploHash exemplo = new ExemploHash();

		/*
		 *  Para mensagens de texto (String)
		 */
		
		byte[] mensagem = "Mensagem para gerar códigos de hash".getBytes(StandardCharsets.UTF_8);
		
		// Gerando hash SHA1
	    System.out.println("\n -------------- \n SHA 1 \n --------------");
		byte[] hashSHA1 = exemplo.gerarHashString(mensagem, "SHA-1");
		System.out.println(exemplo.byteToString(hashSHA1));
		
		// Gerando hash SHA-256
	    System.out.println("\n -------------- \n SHA-256 \n --------------");
		byte[] hashSHA256 = exemplo.gerarHashString(mensagem, "SHA-256");
		System.out.println(exemplo.byteToString(hashSHA256));
		
		// Gerando hash MD5
	    System.out.println("\n -------------- \n MD5 \n --------------");
		byte[] hashMD5 = exemplo.gerarHashString(mensagem, "MD5");
		System.out.println(exemplo.byteToString(hashMD5));
		

		/*
		 *  Para arquivos (binários)
		 *  
		 *  Comprovante:
		 *  
		 *  > md5sum commons-crypto-1.0.0.jar (Ubuntu)
		 *  > 981c95e38457b10d429090496b96f2d6  commons-crypto-1.0.0.jar
		 */
		
		File arquivoTeste = new File("src/rep/commons-crypto-1.0.0.jar");
		
		// Gerando hash SHA1
	    System.out.println("\n -------------- \n SHA 1 - ARQUIVOS \n --------------");
		System.out.println(exemplo.gerarHashFile(arquivoTeste, "SHA-1"));
		
		// Gerando hash SHA-256
	    System.out.println("\n -------------- \n SHA-256 - ARQUIVOS  \n --------------");
		System.out.println(exemplo.gerarHashFile(arquivoTeste, "SHA-256"));
		
		// Gerando hash MD5
	    System.out.println("\n -------------- \n MD5 - ARQUIVOS \n --------------");
		System.out.println(exemplo.gerarHashFile(arquivoTeste, "MD5"));
		
	}

}
