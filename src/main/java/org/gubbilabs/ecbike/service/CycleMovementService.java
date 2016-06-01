package org.gubbilabs.ecbike.service;

import org.gubbilabs.ecbike.domain.Authority;
import org.gubbilabs.ecbike.domain.Bicycle;
import org.gubbilabs.ecbike.domain.CycleToCustomerMapper;
import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;
import org.gubbilabs.ecbike.domain.CycleToStockNodeMapper;
import org.gubbilabs.ecbike.domain.MemberMobile;
import org.gubbilabs.ecbike.domain.RentalBufferNode;
import org.gubbilabs.ecbike.domain.User;
import org.gubbilabs.ecbike.domain.util.MoveCycleResponseJsonObject;
import org.gubbilabs.ecbike.domain.util.MoveStockToRentalNodeMapper;
import org.gubbilabs.ecbike.repository.AuthorityRepository;
import org.gubbilabs.ecbike.repository.BicycleRepository;
import org.gubbilabs.ecbike.repository.CycleToCustomerMapperRepository;
import org.gubbilabs.ecbike.repository.CycleToRentalNodeMapperRepository;
import org.gubbilabs.ecbike.repository.CycleToStockNodeMapperRepository;
import org.gubbilabs.ecbike.repository.RentalBufferNodeRepository;
import org.gubbilabs.ecbike.repository.UserRepository;
import org.gubbilabs.ecbike.security.SecurityUtils;
import org.gubbilabs.ecbike.service.util.RandomUtil;
import org.gubbilabs.ecbike.web.rest.util.HeaderUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;

import javax.inject.Inject;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class CycleMovementService {

	private final Logger log = LoggerFactory.getLogger(CycleMovementService.class);

	@Inject
	private CycleToStockNodeMapperRepository cycleToStockNodeMapperRepository;
	@Inject
	private BicycleRepository bicycleRepository;
	@Inject

	private CycleToRentalNodeMapperRepository cycleToRentalNodeMapperRepository;
	@Inject
	private RentalBufferNodeRepository rentalBufferNodeRepository;
	@Inject
	private CycleToCustomerMapperRepository cycleToCustomerMapperRepository;

	@Timed
	@Transactional
	public MoveCycleResponseJsonObject  moveCycleToNode(MoveStockToRentalNodeMapper m) {

		log.debug("MoveStockToRentalNodeMapper " + m);
		String b = m.getBicycleId();
		Long nodeId = m.getRentalNodeId();
		Long stockId = m.getStockNodeId();
		
		MoveCycleResponseJsonObject responseObject  = new MoveCycleResponseJsonObject();
		
		//
		Bicycle b2 = bicycleRepository.findByTagId(b);
		if (b2 == null)
		{
			responseObject.setStatus("NOTOK");
			responseObject.setBicycleId(0);
			responseObject.setCode(400);
			responseObject.setMessage("Bicycle with Tag not found "+ b);
			responseObject.setRentalNodeMapperId(nodeId);
			responseObject.setStockNodeMapperId(stockId);
			return responseObject;
			
		}
			 

		log.debug("found cycle " + b2.getId() + "TAG" + b2.getTagId());
     	CycleToStockNodeMapper map1 =
		 cycleToStockNodeMapperRepository.findEntryForCycleAtStock(stockId,
		 b2.getId());

		 
		 if (map1 == null)
			{
				responseObject.setStatus("NOTOK");
				responseObject.setBicycleId( b2.getId());
				responseObject.setCode(401);
				responseObject.setMessage("CycleToStockNodeMapper     not found "+ stockId + b2.getId() );
				responseObject.setRentalNodeMapperId(nodeId);
				responseObject.setStockNodeMapperId(stockId);
				return responseObject;
				
			}
		RentalBufferNode rentalBufferNode = null;
		try {
			rentalBufferNode = rentalBufferNodeRepository.findById(nodeId);
			if (rentalBufferNode == null) {
				responseObject.setBicycleId( b2.getId());
				responseObject.setCode(403);
				responseObject.setStatus("NOTOK");
				responseObject.setMessage("rentalBufferNode     not found "+ nodeId  );
				responseObject.setRentalNodeMapperId(nodeId);
				responseObject.setStockNodeMapperId(stockId);
				return responseObject;
			} else {
				log.debug("MoveStockToRentalNodeMapper  rentalBufferNode" + rentalBufferNode.getStationName());
			}
		} catch (Exception e) {
			log.debug("MoveStockToRentalNodeMapper  Exception" + e);
		}

		cycleToStockNodeMapperRepository.delete(map1.getId());
		b2.setMoveStatus(2);
		Bicycle resBicycle = bicycleRepository.save(b2);

		CycleToRentalNodeMapper saveToMap = new CycleToRentalNodeMapper();
		saveToMap.setMovedCycle(b2);
		saveToMap.setNodeDest(rentalBufferNode);

		// ADD TO THE cycleto rental node repository;
		CycleToRentalNodeMapper result = cycleToRentalNodeMapperRepository.save(saveToMap);
		responseObject.setBicycleId( b2.getId());
		responseObject.setCode(200);
		responseObject.setStatus("OK");
		responseObject.setMessage("Succes "+ resBicycle.getTagId() +"Updated" + result.getId() +"Created Mapping "  );
		responseObject.setRentalNodeMapperId(nodeId);
		responseObject.setStockNodeMapperId(stockId);
		return responseObject;
	}

	public void moveCycleFromNodeToMember(Bicycle rentedBicycle, MemberMobile rentedMember,
			RentalBufferNode startNode) {
		
		rentedBicycle.setMoveStatus(3);
		Bicycle resBicycle = bicycleRepository.save(rentedBicycle);
		
		
		CycleToRentalNodeMapper map1 =
				 cycleToRentalNodeMapperRepository.findEntryForCycleAtNode(startNode.getId(),
						 rentedBicycle.getId());
		
		cycleToRentalNodeMapperRepository.delete(map1.getId());
		
		CycleToCustomerMapper saveToMap = new CycleToCustomerMapper();
		saveToMap.setMovedCycle(rentedBicycle); 
		saveToMap.setRentedCustomer(rentedMember);
		
		CycleToCustomerMapper result = cycleToCustomerMapperRepository.save(saveToMap);
		
		
		
	 	
	 
		
	}

	public void moveCycleFromMemberToNode(Bicycle rentedBicycle, MemberMobile rentedMember, RentalBufferNode stopNode) {
 		
		
		rentedBicycle.setMoveStatus(2);
		Bicycle resBicycle = bicycleRepository.save(rentedBicycle);
		
		
		CycleToCustomerMapper map1 =
				 cycleToCustomerMapperRepository.findEntryForCycleAtCustomer(rentedMember.getId(),
						 rentedBicycle.getId());
		
		cycleToCustomerMapperRepository.delete(map1.getId());
		
		CycleToRentalNodeMapper saveToMap = new CycleToRentalNodeMapper();
		saveToMap.setMovedCycle(resBicycle);
		saveToMap.setNodeDest(stopNode);
		
		CycleToRentalNodeMapper result = cycleToRentalNodeMapperRepository.save(saveToMap);
		
	}

}
