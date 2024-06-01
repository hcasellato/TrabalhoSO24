// ARQUIVO DE TESTE
// Use quando estiver em dúvida na mudança em SubmissionInterface.submitJob
public class Tester{
     public static void main(String[] args)
     {
        System.out.println("Testador sintático de arquivos!\nRealizando testes:\n");
        
        System.out.print("Verdadeiro positivo: ");
        passou(SubmissionInterface.submitJob("listaDeProcessos/processo_1.txt"));
        
        System.out.print("Erro de bloco: ");
        passou(!SubmissionInterface.submitJob("listaDeProcessos/processo_erro_bloco.txt"));
    
        System.out.print("Erro de execute: ");
        passou(!SubmissionInterface.submitJob("listaDeProcessos/processo_erro_execute.txt"));
    
        System.out.print("Erro de espaco: ");
        passou(!SubmissionInterface.submitJob("listaDeProcessos/processo_erro_whitespace.txt"));
    }

    public static void passou(boolean submission)
    {
        if(submission)
        {
            System.out.println("\033[1;92mPASSOU\033[0m");
        }else{
            System.out.println("\033[1;91mNÃO PASSOU\033[0m");
        }
    }
}