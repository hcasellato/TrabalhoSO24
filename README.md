# Simulador de Escalonamento de Processos
Este projeto é um simulador de um sistema de escalonamento de processos, projetado para demonstrar os conceitos de escalonamento preemptivo por prioridade e execução de processos baseada em quantum. Ele consiste em vários componentes que trabalham juntos para gerenciar o envio, escalonamento e execução de processos.

## Estrutura do Projeto

- `app/`: Contém a classe principal da aplicação `SchedulerSimulator`.
- `interfaces/`: Define as interfaces para desacoplar componentes e facilitar a extensibilidade.
  - `ControlInterface`: Interface para controlar a simulação (iniciar, parar, suspender, retomar).
  - `InterSchedulerInterface`: Interface para comunicação entre os escalonadores de longo e curto prazo.
  - `SubmissionInterface`: Interface para enviar novos processos para o sistema..
  - `NotificationInterface`: Interface para exibir notificações e atualizações de status..
- `objects/`: Contém a classe `Proces`, que representa um processo no sistema.
- `schedulers/`: Contém as implementações dos escalonadores de longo e curto prazo.
  - `LongTermScheduler`: Responsável por gerenciar a fila de envio e despachar processos para o escalonador de curto prazo..
  - `ShortTermScheduler`: Implementa escalonamento preemptivo por prioridade e execução de processos baseada em quantum..
- `services/`: Contém serviços auxiliares usados pelo sistema.
  - `ConsoleNotifier`: Implementação da`NotificationInterface` fpara exibir notificações no console.
  - `Parsing`: Serviço para analisar definições de processo de arquivos.
  - `ProcessControlService`: WWrapper para a classe `Process`, fornecendo controle adicional e gerenciamento de estado.
- `userinterface/`: C Contém a classe`UserInterface`, que fornece uma interface gráfica para interagir com o sistema.
- `test/`: CContém casos de teste para vários componentes do sistema

## Interações de Classe

A classe `SchedulerSimulator` é o ponto de entrada da aplicação. Ela cria instâncias de `LongTermScheduler`, `ShortTermScheduler` e `UserInterface` e os conecta usando as interfaces.

O LongTermScheduler gerencia a fila de envio e despacha processos para o ShortTermScheduler. Ele usa o serviço Parsing para analisar as definições de processo de arquivos enviados através da UserInterface.

O ShortTermScheduler implementa o algoritmo de escalonamento preemptivo por prioridade e a execução de processos baseada em quantum. Ele mantém três filas de prontos (uma para cada nível de prioridade) e uma fila de bloqueados. Os processos são executados com base em sua prioridade, e se um processo de prioridade mais alta chegar, o processo em execução é preemptado. Os processos também são preemptados quando seu quantum expira e são movidos para uma fila de prioridade mais baixa. Processos bloqueados são tratados movendo-os para a fila de bloqueados e, em seguida, de volta para a fila de prontos apropriada quando seu tempo de bloqueio expira.

A UserInterface fornece uma interface gráfica para interagir com o sistema. Os usuários podem enviar novos processos, iniciar, parar, suspender e retomar a simulação e visualizar o status das filas e dos processos em execução.

O ConsoleNotifier é uma implementação da NotificationInterface e exibe notificações e atualizações de status no console.

## Algoritmos de Escalonamento

O ShortTermScheduler implementa um algoritmo de escalonamento preemptivo por prioridade com três níveis de prioridade. Os processos são executados com base em sua prioridade, com processos de prioridade mais alta preemptando os de prioridade mais baixa.

Além disso, uma abordagem baseada em quantum é usada para a execução do processo. Cada processo recebe um certo número de quanta (fatias de tempo) com base em seu nível de prioridade. Quando o quantum de um processo expira, ele é preemptado e movido para uma fila de prioridade mais baixa. Os valores de quantum para cada nível de prioridade são:

Prioridade 1: 12 quanta
Prioridade 2: 6 quanta
Prioridade 3: 6 quanta
Se um processo bloqueado for retomado e tiver consumido menos da metade de seus quanta restantes, ele será movido para a fila de prioridade 2 com um novo quantum de 6. Caso contrário, ele será movido de volta para a fila de prioridade 3.

## Executando a Aplicação

Para executar a aplicação, execute a classe SchedulerSimulator. A UserInterface será exibida, permitindo que você envie novos processos, inicie, pare, suspenda e retome a simulação e visualize o status das filas e dos processos em execução.

Para enviar um novo processo, digite o caminho do arquivo de definição do processo no campo "Caminho do Programa" e clique no botão "Enviar Programa". O arquivo de definição do processo deve seguir um formato específico, com instruções como executar e bloquear entre um bloco iniciar e fim.



