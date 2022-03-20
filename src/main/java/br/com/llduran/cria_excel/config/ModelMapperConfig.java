package br.com.llduran.cria_excel.config;

import br.com.llduran.cria_excel.model.CompraFinalizada;
import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ModelMapperConfig
{
	@Bean
	public ModelMapper modelMapper()
	{
		var modelMapper = new ModelMapper();

		var compraFinalizadaToCompraFinalizadaDTOTypeMap = modelMapper.createTypeMap(CompraFinalizada.class, CompraFinalizadaDTO.class);
		compraFinalizadaToCompraFinalizadaDTOTypeMap.<Integer>addMapping(compraSrc -> compraSrc.getCompraChave().getCompra().getCodigoPassagem(),(compraDest, value) -> compraDest.setCodigoPassagem(value));
		compraFinalizadaToCompraFinalizadaDTOTypeMap.<String>addMapping(compraSrc -> compraSrc.getCompraChave().getCompra().getNroCartao(), (compraDest, value) -> compraDest.setNroCartao(value));
		compraFinalizadaToCompraFinalizadaDTOTypeMap.<Integer>addMapping(compraSrc -> compraSrc.getCompraChave().getCompra().getCodigoSegurancaCartao(), (compraDest, value) -> compraDest.setCodigoSegurancaCartao(value));
		compraFinalizadaToCompraFinalizadaDTOTypeMap.<String>addMapping(compraSrc -> compraSrc.getCompraChave().getCompra().getValorPassagem(), (compraDest, value) -> compraDest.setValorPassagem(value));
		compraFinalizadaToCompraFinalizadaDTOTypeMap.<LocalDate>addMapping(compraSrc -> compraSrc.getCompraChave().getCompra().getDataViagem(), (compraDest, value) -> compraDest.setDataViagem(value));

		return modelMapper;
	}
}
