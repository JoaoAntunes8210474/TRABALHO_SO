DEADLINE ------ 15 JAN 23:59
ProjectName --- so_grupoX.zip


SeccaoCritica

	- Comboio sair da estacao 		  

ObjetoPartilhado

	- Linhas
	- Filewriter
	- Estacoes (talvez)

SituacaoCompeticao 

	- Determinar quem sai da estacao 1º
	- c/ parametro de countAtrasos (para prioritizar!) XXXXX NOT_IMPLEMENT

Sincronizacao 

	- Bloquear o acesso as linhas mediante o que entrar 1º



Conflitos	

	- 2 comboios em sentidos opostos na mesma linha
	- estacao sobrelotada (max comboios)
	- countComboios
	- count atualizado e comparado com o max

EmbarquePassageiros	

	- c/bilhete válido
	- count atualizado e comparado com o maxPassageiros


Comboio	 	
	
	- maxPassageiros
	- horario

Horario	 

	- nomeEstacao
	- horaChegada
	- horaPartida
	- direcao
	- chegouDestino


Estacao

	- tem 1 linha
	- max 2 linhas
	- tem comboios ( countComboio )
	- c/passageiros





Requisitos

 	- Modulo Main
		- Processamento das decisoes principais decorrentes do seu funcionamento
			- Manter as estrutras de dados e respetiva validacao
			- Controlo das simulacoes
				- Validacao das viagens
				- Ordem de entrada dos passageiros
				- Ordem para o comboio iniciar viagem
				- Indicacao da estado da ocupacao dos comboios
		- Lancar a execucao dos seus sub-modelos (prbly threads - janelas, iniciar os comboios)
		- Termino das threads
		- Os sub-modelos n devem efetuar validacoes relativas a operacao do sistema de gestao de trafico
		
	- Simulador de trafego (threads)
		- Recebe um descricao das linhas
		- Dos comboios
		- Dos horarios
		- Dos passageiros embarcados
			- Com estes parametros simula o andamento dos comboios (Construtor)
		- Regista os conflitos num ficheiro chamado log
		- Saida dos comboios FCFS

	- Modulo de embarque de passageiros
		- deve simmular e validar a entrada de passageiros
			- controlado pelo Main
				- Controla as portas 
			- passageiro com viagem mais curta é que entra

	- Painel de controlo
		- UI
		- apresenta informacao:
				- nº de passageiros embarcado
				- nº de comboios na rede
					- horarios
				- conflitos identificados
				- duracao das viagens
				- opcao para alterar os horarios
		- permite interagir com o sistema atraves de um menu de opcoes

	- Modulo de gestao de conflitos
		- tratar conflitos (como sobrelotacao, etc) com alteracao de horarios
