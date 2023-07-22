package br.com.llduran.cria_excel.builders;

import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;

import java.time.LocalDate;

public class CompraFinalizadaBuilder
{
	private CompraFinalizadaDTO compraFinalizada;

	private CompraFinalizadaBuilder(){}

	public static CompraFinalizadaBuilder umaCompraFinalizada()
	{
		CompraFinalizadaBuilder builder = new CompraFinalizadaBuilder();
		builder.compraFinalizada = new CompraFinalizadaDTO();
		builder.compraFinalizada.setChave("DBA18CD0-A11A-5E7D-D64E-6601093D4C17");
		builder.compraFinalizada.setCompraCodigoPassagem(6840);
		builder.compraFinalizada.setCompraNroCartao("6334238874556822539");
		builder.compraFinalizada.setCompraCodigoSegurancaCartao(280);
		builder.compraFinalizada.setCompraValorPassagem("R$23,93");
		builder.compraFinalizada.setCompraDataViagem(LocalDate.of(2023, 01, 24));
		builder.compraFinalizada.setCompraMensagem("PWQ91LKG2UC");
		builder.compraFinalizada.setCompraPagamentoOK(false);

		return builder;
	}

	public static CompraFinalizadaBuilder outraCompraFinalizada()
	{
		CompraFinalizadaBuilder builder = new CompraFinalizadaBuilder();
		builder.compraFinalizada = new CompraFinalizadaDTO();
		builder.compraFinalizada.setChave("916A4577-292B-21BF-9033-1A5D4166C3D4");
		builder.compraFinalizada.setCompraCodigoPassagem(6128);
		builder.compraFinalizada.setCompraNroCartao("6767 8453 1414 2432");
		builder.compraFinalizada.setCompraCodigoSegurancaCartao(597);
		builder.compraFinalizada.setCompraValorPassagem("R$96,08");
		builder.compraFinalizada.setCompraDataViagem(LocalDate.of(2021, 06, 21));
		builder.compraFinalizada.setCompraMensagem("POS78NVU8NI");
		builder.compraFinalizada.setCompraPagamentoOK(false);

		return builder;
	}

	public static CompraFinalizadaBuilder maisOutraCompraFinalizada()
	{
		CompraFinalizadaBuilder builder = new CompraFinalizadaBuilder();
		builder.compraFinalizada = new CompraFinalizadaDTO();
		builder.compraFinalizada.setChave("8125830A-DA6C-8D84-1B69-39C8551AD8DE");
		builder.compraFinalizada.setCompraCodigoPassagem(7965);
		builder.compraFinalizada.setCompraNroCartao("491 77649 71875 433");
		builder.compraFinalizada.setCompraCodigoSegurancaCartao(251);
		builder.compraFinalizada.setCompraValorPassagem("R$63,44");
		builder.compraFinalizada.setCompraDataViagem(LocalDate.of(2021, 10, 06));
		builder.compraFinalizada.setCompraMensagem("KAY23JOR9XJ");
		builder.compraFinalizada.setCompraPagamentoOK(true);

		return builder;
	}

	public CompraFinalizadaDTO agora() { return compraFinalizada; }

}
