
package implementacoes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import swing.janelas.PDI_Lote;


public class Greenness {
	
	public static float[] RGBtoYIQ(int red, int green, int blue){
        float[] yiq = new float[3];
        float y,i,q;
        
        float r = (float)red; 
        float g = (float)green;
        float b = (float)blue;
        
        y = (float)(0.299 * r + 0.587 * g + 0.114 * b);
        i = (float)(0.596 * r - 0.275 * g - 0.322 * b);
        q = (float)(0.212 * r - 0.523 * g + 0.311 * b);
        
        yiq[0] = y;
        yiq[1] = i;
        yiq[2] = q;
        
        
        return yiq;
    }

    public static int[] YIQtoRGB(double y, double i, double q){
        int[] rgb = new int[3];
        int r,g,b;
        
        System.out.println("Valores do YIQ antes "+" "+ y +" " +" "+ i +" "+ q);
 
        r = (int)((1.000* y + 0.956 * i + 0.621 * q)); 
        g = (int)((1.000* y - 0.272 * i - 0.647 * q));
        b = (int)((1.000* y - 1.105 * i + 1.702 * q));
        
 
        r = Math.max(0,Math.min(255,r));
        g = Math.max(0,Math.min(255,g));
        b = Math.max(0,Math.min(255,b));

        
        
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;

        System.out.println("Valores do RGB depois "+ " "+ rgb[0] + " "+ rgb[1] + " "+ rgb[2]);
        
        return rgb;
    }

	/**
	 * Essa função é a implementação da método de GreennesskG = kG − (R + B)
	 * onde K é o valor passado pelo usuário e o R,G e B são os valores obtido da imagem
	 * 
	 * @param img A imagem onde o filtro será aplicado
	 * @param K O valor K da equação
	 * @return retorna a imagem depois de aplicado o filtro
	 */
    
	public BufferedImage GreennKG(BufferedImage img, float K) {
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		//COMEÇO NORMALIZAÇAO
		double a = K;
		double Lmax = 255;

        float[] yiq = new float[3];
        float[] yiqe_aux = new float[3];

        
		// Formula fica mais ou menos assim:
		// sabendo que x � o valor do pixel atual:
		// xe igual vizinhos!

		for (int i = 1; i < img.getHeight()-1; i++) {
			for (int j = 1; j < img.getWidth()-1; j++) {
				Color x = new Color (img.getRGB(j, i));
				
				System.out.println("Lendo o pixel:  "+ x + " da Cordenada : x"+i+" y"+j);
				
				yiq = RGBtoYIQ(x.getRed(),x.getGreen(), x.getBlue());
				
				float y = yiq[0];
				
				double xe = 0;
				double contrast = 0;
				double contrastaux = 0;
				float ye = 0;
				int vizinhoimin ,vizinhojmin ;
	
				for(vizinhoimin = i-1 ; vizinhoimin <= i+1 ; vizinhoimin++) {
					for (vizinhojmin = j-1; vizinhojmin <= j+1; vizinhojmin++) {
						
						Color xe_aux = new Color (img.getRGB(vizinhojmin, vizinhoimin));
						
						yiqe_aux = RGBtoYIQ(xe_aux.getRed(),xe_aux.getGreen(), xe_aux.getBlue());
						
						float ye_aux = yiqe_aux[0];
						
//						xe =+ xe + somatorio;
						ye =+ ye_aux ;
					}
				}
				
			    System.out.println("Valores do YIQ depois "+ " "+ yiq[0] + " "+ yiq[1] + " "+ yiq[2]);
							
				ye = ye - y;
				ye = ye/8;
				
//				xe = xe - x.getBlue();
//				xe = xe/8;
								
//				if(x.getBlue() <= xe) {
				if(y <= ye) {
//				den e num representam denominador e numerador (no caso invertido pq sou burro)
					
//					double den = xe - x.getBlue();
//					double num = xe + x.getBlue();
					
					double den = ye - y;
					double num = ye + y;

					if(num == 0 ) {
						contrast = 0;
					}
					else {
						contrast = den/num;
					}
					
					double aux = 0;
					int contadorContrast = 0;
					
					if (contrast != 0){
					
						while(contadorContrast != 2) {
							
							if(contrastaux != 0) {
								
								aux = contrast;
								
								contrast = ((1-a)*contrastaux)+(a*Math.pow(contrastaux, 2));
								
								contrastaux = aux;
								
								contadorContrast ++;
								
							}
							else
								contrastaux = contrast; 
						}
					}
					
					// Metodo Contraste com fun��o polinomial

//					contrast = Math.pow(contrast, a); // Funcao potencia 1 do artigo
		
					den = (1-contrast);
					den = den*xe;
					num = (1+contrast);

//					double xaux = den/num;
//					xaux = Math.floor(xaux) + 1 ;
					double yaux = den/num;
					yaux = Math.floor(yaux) + 1;
					
					// Convertendo a imagem de volta
					yiq[0] = (float)yaux;
					int[] rgb = new int[3];
					rgb = YIQtoRGB(yiq[0],yiq[1],yiq[2]);
						
					// Setar o pixel da imagem 
		
					Color novo = new Color((int)rgb[0], (int)rgb[1], (int)rgb[2]);
					res.setRGB(j, i, novo.getRGB());


				}
				else {

//					double den = testando - xe;
//					double num = 510 - testando - xe;
					double den = y - ye;
					double num = 510 - y - ye;
					
					if(num == 0 ) {
						contrast = 0;
					}
					else {
						contrast = den/num;
					}
					
					double aux = 0;
					int contadorContrast = 0;
					// Metodo Contraste com fun��o polinomial
					if (contrast != 0){
						
						while(contadorContrast != 2) {
							
							if(contrastaux != 0) {
								
								aux = contrast;
								
								contrast = ((1-a)*contrastaux)+(a*Math.pow(contrastaux, 2));
								
								contrastaux = aux;
								
								contadorContrast ++;
								
							}
							else
								contrastaux = contrast; 
						}
					}
										
					// FUN��O RAIZ NAO APAGAR!!
//					contrast = Math.pow(contrast,a);
					
//					den = (Lmax - xe)*(1-contrast);
//					num = (1+contrast);
					den = (Lmax - ye)*(1-contrast);
					num = (1+contrast);

					
/*					double xaux = Lmax-(den/num);
					xaux = Math.floor(xaux) + 1 ;
*/
					double yaux = Lmax-(den/num);
/*
					if (xaux > 255) {
						xaux = 255;
					}
					else if (xaux < 0 ) {
						xaux = 0;
					} 
*/
					
					if (yaux > 255) {
						yaux = 255;
					}
					else if (yaux < 0 ) {
						yaux = 0;
					}
					
					// Convertendo a imagem de volta
					yiq[0] = (float)yaux;
					int[] rgb = new int[3];
					rgb = YIQtoRGB(yiq[0],yiq[1],yiq[2]);
						
					// Setar o pixel da imagem 
		
					Color novo = new Color((int)rgb[0], (int)rgb[1], (int)rgb[2]);
					res.setRGB(j, i, novo.getRGB());

				}	

			}
		}
	return res;

	}
}
