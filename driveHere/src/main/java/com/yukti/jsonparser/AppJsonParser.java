package com.yukti.jsonparser;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.yukti.dataone.model.AuctionNameResult;
import com.yukti.dataone.model.AuctionSearch;
import com.yukti.dataone.model.Leasedata;
import com.yukti.dataone.model.NumberOfMissCar;
import com.yukti.dataone.model.ParentNode;
import com.yukti.dataone.model.ReportResult;
import com.yukti.dataone.model.Simple;
import com.yukti.driveherenew.ResVersionModel;
import com.yukti.driveherenew.StageDetail;
import com.yukti.driveherenew.Stagearraydetail;
import com.yukti.driveherenew.Stagewithlevel;
import com.yukti.driveherenew.search.LatLong;
import com.yukti.driveherenew.search.Search;
import com.yukti.utils.AllDetail;
import com.yukti.utils.SearchContract;

public class AppJsonParser {

	public Verify getIsVerify(JSONObject response) {
		Gson gson = new Gson();
		Verify orm = gson.fromJson(response.toString(), Verify.class);
		return orm;
	}

	public Stagearraydetail stagearraydetailResult(JSONObject response) {
		Gson gson = new Gson();
		Stagearraydetail orm = gson.fromJson(response.toString(),
				Stagearraydetail.class);
		return orm;
	}

	public LatLong getlatlong(JSONObject response) {
		Gson gson = new Gson();
		LatLong orm = gson.fromJson(response.toString(), LatLong.class);
		return orm;
	}

	public LatLong getlatlong(String response) {
		Gson gson = new Gson();
		LatLong orm = gson.fromJson(response.toString(), LatLong.class);
		return orm;
	}
	public ResVersionModel getVersionResponse(JSONObject response) {
		Gson gson = new Gson();
		ResVersionModel orm = gson.fromJson(response.toString(),
				ResVersionModel.class);
		return orm;
	}
	public ResVersionModel getVerResponse(String response) {
		Gson gson = new Gson();
		ResVersionModel orm = gson.fromJson(response.toString(),
				ResVersionModel.class);
		return orm;
	}
	public Login login(JSONObject response) {
		Gson gson = new Gson();
		Login orm = gson.fromJson(response.toString(), Login.class);
		return orm;

	}
	public Login login(String response) {
		Gson gson = new Gson();
		Login orm = gson.fromJson(response, Login.class);
		return orm;

	}

	public Signup singup(JSONObject response) {
		Gson gson = new Gson();
		Signup orm = gson.fromJson(response.toString(), Signup.class);
		return orm;

	}
	public Signup singup(String response) {
		Gson gson = new Gson();
		Signup orm = gson.fromJson(response, Signup.class);
		return orm;

	}


	public GeneralOrm general(JSONObject response) {
		Gson gson = new Gson();
		GeneralOrm orm = gson.fromJson(response.toString(), GeneralOrm.class);
		return orm;

	}
	public GeneralOrm general(String response) {
		Gson gson = new Gson();
		GeneralOrm orm = gson.fromJson(response, GeneralOrm.class);
		return orm;

	}

	public FindMatch findMatch(JSONObject response) {
		Gson gson = new Gson();
		FindMatch orm = gson.fromJson(response.toString(), FindMatch.class);
		return orm;
	}

	public FindMatch findMatch(String response) {
		Gson gson = new Gson();
		FindMatch orm = gson.fromJson(response, FindMatch.class);
		return orm;
	}

//	public AddCarResponse addCarResponse(JSONObject response) {
//		Gson gson = new Gson();
//		AddCarResponse addCarRes = gson.fromJson(response.toString(),
//				AddCarResponse.class);
//		return addCarRes;
//	}

	public AddCarResponse addCarResponse(String response) {
		Gson gson = new Gson();
		AddCarResponse addCarRes = gson.fromJson(response,
				AddCarResponse.class);
		return addCarRes;
	}
	public UpdateResponse update(JSONObject response) {
		Gson gson = new Gson();
		UpdateResponse orm = gson.fromJson(response.toString(),
				UpdateResponse.class);
		return orm;
	}
	public UpdateResponse update(String response) {
		Gson gson = new Gson();
		UpdateResponse orm = gson.fromJson(response.toString(),
				UpdateResponse.class);
		return orm;
	}
	public ParentNode parseDataoneResponse(JSONObject response) {
		Gson gson = new Gson();
		ParentNode orm = gson.fromJson(response.toString(), ParentNode.class);
		return orm;

	}

	public ParentNode parseDataoneResponse(String response) {
		Gson gson = new Gson();
		ParentNode orm = gson.fromJson(response, ParentNode.class);
		return orm;

	}

	public Simple parseData(JSONObject response) {
		Gson gson = new Gson();
		Simple orm = gson.fromJson(response.toString(), Simple.class);
		return orm;

	}
	public Simple parseData(String response) {
		Gson gson = new Gson();
		Simple orm = gson.fromJson(response.toString(), Simple.class);
		return orm;

	}

	public Search search(JSONObject response) {
		Gson gson = new Gson();
		Search orm = gson.fromJson(response.toString(), Search.class);
		return orm;
	}

	public Search search(String response) {
		Gson gson = new Gson();
		Search orm = gson.fromJson(response.toString(), Search.class);
		return orm;
	}

	public AllDetail getAllDataResponse(String response) {
		Gson gson = new Gson();
		AllDetail orm = gson.fromJson(response.toString(), AllDetail.class);
		return orm;
	}

	public SearchContract searchcontract(String response) {
		Gson gson = new Gson();
		SearchContract orm = gson.fromJson(response.toString(), SearchContract.class);
		return orm;
	}

	public AuctionSearch actoinsearch(String response) {
		Gson gson = new Gson();
		AuctionSearch orm = gson.fromJson(response.toString(), AuctionSearch.class);
		return orm;
	}

	public ReportResult reportdata(JSONObject response) {
		Gson gson = new Gson();
		ReportResult orm = gson.fromJson(response.toString(),
				ReportResult.class);
		return orm;
	}
	public ReportResult reportdata(String response) {
		Gson gson = new Gson();
		ReportResult orm = gson.fromJson(response,
				ReportResult.class);
		return orm;
	}

	public AuctionNameResult auctiondata(String response) {
		Gson gson = new Gson();
		AuctionNameResult orm = gson.fromJson(response, AuctionNameResult.class);
		return orm;
	}

	public StageDetail stagedata(JSONObject response) {
		Gson gson = new Gson();
		StageDetail orm = gson.fromJson(response.toString(), StageDetail.class);
		return orm;
	}

	public StageDetail stagedata(String response) {
		Gson gson = new Gson();
		StageDetail orm = gson.fromJson(response.toString(), StageDetail.class);
		return orm;
	}

	public Stagewithlevel stageleveldata(JSONObject response) {
		Gson gson = new Gson();
		Stagewithlevel orm = gson.fromJson(response.toString(),
				Stagewithlevel.class);
		return orm;
	}
	public Stagewithlevel stageleveldata(String response) {
		Gson gson = new Gson();
		Stagewithlevel orm = gson.fromJson(response.toString(),
				Stagewithlevel.class);
		return orm;
	}

	public ReportResult missingCardata(JSONObject response) {
		Gson gson = new Gson();
		ReportResult orm = gson.fromJson(response.toString(),
				ReportResult.class);
		return orm;
	}

	public Leasedata leasedata(JSONObject response) {
		Gson gson = new Gson();
		Leasedata orm = gson.fromJson(response.toString(), Leasedata.class);
		return orm;
	}

	public NumberOfMissCar numberOfMissCar(JSONObject response) {
		Gson gson = new Gson();
		NumberOfMissCar orm = gson.fromJson(response.toString(),
				NumberOfMissCar.class);
		return orm;
	}

	public AddTitleResponse addTitleResponse(String response) {
		Gson gson = new Gson();
		AddTitleResponse addTitleRes = gson.fromJson(response.toString(),
				AddTitleResponse.class);
		return addTitleRes;
	}

	public AddWebPicResponse addWebPicResponse(String response) {
		Gson gson = new Gson();
		AddWebPicResponse addWebPicRes = gson.fromJson(response.toString(),
				AddWebPicResponse.class);
		return addWebPicRes;
	}

	public UpdateResponse updateResponse(String response) {
		Gson gson = new Gson();
		UpdateResponse orm = gson.fromJson(response, UpdateResponse.class);
		return orm;

	}

}
