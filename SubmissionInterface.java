import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public interface SubmissionInterface {
    static boolean submitJob(String fileName)
    {
        // Reads file
        try {
            // pega arquivo e o lê
            File arqProcess = new File(fileName);
            Scanner myReader = new Scanner(arqProcess);
            
            // primeiro testa se começa com nome do arquivo
            String title = fileName.split("/")[1];
            //System.out.println(!myReader.nextLine().matches("program " + title));
            if(!myReader.nextLine().matches("program " + title)){
                //System.out.println("ERRO: programa não começa com nome do arquivo");
                myReader.close();
                return false;
            }

            // testa se começa com begin
            if(!myReader.nextLine().matches("begin")){
                //System.out.println("ERRO: Programa não começou com \"begin\"!");
                myReader.close();
                return false;
            }

            int nLine = 2; // Para correlação de erro com linha!

            // agora testa execute e block
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                nLine = nLine + 1;

                if( !data.matches("execute") &&      // testa execute
                    !data.matches("block [1-5]") &&  // testa block com espaço de tempo
                    !data.matches("end"))            // testa se termina em end
                    {
                        //System.out.println("ERRO SINTATICO: [" + nLine + "] " + data);
                        myReader.close();
                        return false;
                }

            }
            
            myReader.close();
          } catch (FileNotFoundException e) { // Caso não consiga ler o arquivo duh
            System.out.println("Arquivo não conseguiu ser lido.");
            e.printStackTrace();
            return false;
          }
          // à partir de agora, o processo foi devidamente testado e 
          // está de acordo com as normas sintáticas
        return true;
    }
    void displaySubmissionQueue();
}