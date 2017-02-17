
function getEle(eleId) { return document.getElementById(eleId); };

function setDisabled(el, disabled) {
	try { el.disabled = disabled; } catch(E) {}
	if (el.childNodes && el.childNodes.length > 0) {
		for (var x = 0; x < el.childNodes.length; x++) 
			setDisabled(el.childNodes[x], disabled);
	}
};

function RseBrowser(sourceNumber, jsonMeta, jsonData, tableId, jsonURI) {
	this._sourceNumber = sourceNumber;
	this._refreshing = false;
	this._querying   = false;
	this._tableId = tableId;
	this._jsonURI = jsonURI;
	
	// Create enhanced metadata
	this._recMeta = new Array();
	for (var idx = 0; idx < jsonMeta.length; idx++) 
		this._recMeta[idx] = new RseField(this, idx, jsonMeta[idx], this.getJSONField(idx, jsonData));
	
	// Create table
//	var tableHTML = "<form action='whatever'><tr class='a'>";
	var tableHTML = "<tr class='a'>";
	tableHTML += "<th align='center' width='1'>Lock</th><th align='center' width='1'>Field</th>" +
				"<th align='center'>Selection/ Variants</th><th align='center'>Value Selection</th>" +
				"<th align='left' width='100%'>Value</th>";
	tableHTML += "</tr>";//</form>";
	for (var idx = 0; idx < this._recMeta.length; idx++) 
		tableHTML += this._recMeta[idx].createFieldRowContent();
	getEle(tableId).innerHTML = tableHTML;
	
	// Append checkbox locks & create sliders
	for (var idx = 0; idx < this._recMeta.length; idx++) 
		this._recMeta[idx].init();
	
	this.refreshRecord(jsonData);
};

RseBrowser.prototype.refreshRecord = function (jsonData) {
	this._refreshing = true;
//	this.setQuerying(false); 
	for (var idx = 0; idx < jsonData.length; idx++) 
		this._recMeta[jsonData[idx].index].update(jsonData[idx]);
	this._refreshing = false;
};

RseBrowser.prototype.refreshWithUpdatedJSON = function (filterIndex, filterVal, filterAll) {
	if (this._refreshing || this._querying) return false;
	this._refreshing = true;
//	this.setQuerying(true); 
	var url = this._jsonURI + "?sourceNumber=" + this._sourceNumber + "&filterIndex=" + filterIndex + "&filterVal=" + filterVal + "&filterAll=" + filterAll;
	oThis = this;
	$.getJSON(url, function(json) { oThis.refreshRecord(json); });
	return true;
};

RseBrowser.prototype.setQuerying = function (querying) {
	this._querying = querying;
	for (var idx = 0; idx < this._recMeta.length; idx++) 
		this._recMeta[idx].disableSlider(querying); 

//	$(".slider").attr("disabled", refresh);
//	$(".slider-input").attr("disabled", refresh);
};

RseBrowser.prototype.getJSONField = function(index, jsonData) {
	for (var idx = 0; idx < jsonData.length; idx++) 
		if (jsonData[idx].index == index) return jsonData[idx];
	
	var jsonField;
	jsonField.sliderVal = 0;
	jsonField.sliderMax = 0;
	jsonField.fieldVal = "---";
	return jsonField;
};

//===========================================================================================
//
//                            R S E   F I E L D   C L A S S 
//
//===========================================================================================

function RseField(rse, idx, jsonMeta, jsonData) {
	this._rse      = rse;
	this._index    = idx;
	this._jsonMeta = jsonMeta;
	this._jsonData = jsonData;
	this._cleared  = true;
	
	this._lock;
	this._slider;
};

RseField.prototype.getId            = function()     { return this._index;                          };
RseField.prototype.getValFieldId    = function(type) { return type + "-varValue-" + this._index;    };
RseField.prototype.getSliderFieldId = function(type) { return "slider-" + type + "-" + this._index; };

RseField.prototype.getLockFieldId      = function()  { return "lock-" + this._index;                };
RseField.prototype.getCell4LockFieldId = function()  { return "cell4" + this.getLockFieldId();      };
RseField.prototype.getVariantsFieldId  = function()  { return "variants-" + this._index;            };

RseField.prototype.createFieldRowContent = function() {
	var rowClass = (this._index % 2 == 0) ? "a" : "b";
	var result = "<tr class='" + rowClass + "'>" +
			"<td id='" + this.getCell4LockFieldId() + "'></td>" + 
			"<td><label for='field-" + this._index + "'>" + this._jsonMeta.caption + "</label></td>" + 
			"<td id='" + this.getVariantsFieldId() + "' align='center'></td>"; 
	
	// Slider control cell
	result += "<td><div class='slider' ";
	if (!this._jsonMeta.filter) 
		result += "style='display:none;'";
	result += " id='" + this.getSliderFieldId("") + "' tabIndex='" + this.index + "'>";
	result += "<input class='slider-input' id='" + this.getSliderFieldId("input") + "' />";
	result += "</div></td>";
	
	// Value cell
	result += "<td  id='" + this.getSliderFieldId("value") + "'>" +
			  "<img id='" + this.getValFieldId("img") + "' src=''/>" +
			  "<div id='" + this.getValFieldId("txt") + "'></div></td></tr>";
	
	return result;
};

RseField.prototype.init = function() {
	if (this._jsonMeta.filter) {
		this.createLock();
		this.createSlider();
	}
};

RseField.prototype.createLock = function() {
	this._lock         = document.createElement("input");
	this._lock.type    = "checkbox";
	this._lock.id      = this.getLockFieldId();
//	this._lock.checked = true;
	var oThis = this;
	this._lock.onclick = function () { oThis.lockOnChange(); };
	getEle(this.getCell4LockFieldId()).appendChild(this._lock);
};

RseField.prototype.lockOnChange = function() {
	getEle(this.getSliderFieldId("")).style.display = this._lock.checked ? "none" : "block";
	this._rse.refreshWithUpdatedJSON(this._index, this._slider.getValue(), this._lock.checked);
};

RseField.prototype.createSlider = function() {
	this._slider = new Slider(getEle(this.getSliderFieldId("")), getEle(this.getSliderFieldId("input")));
	this._slider.setUnitIncrement(1);
	this._slider.setBlockIncrement(1);
	var oThis = this;
	this._slider.onchange = function () { oThis.sliderOnChange(); };
};

RseField.prototype.sliderOnChange = function() {
	if (this._rse._refreshing) 
		return; // this._slider.setValue(this._jsonData.sliderVal);
	
	var sliderVal = this._slider.getValue();
	if (!this._cleared && this._jsonData.sliderVal != sliderVal) 
		this._rse.refreshWithUpdatedJSON(this._index, sliderVal, false);
};

RseField.prototype.disableSlider = function(disable) {
	if (this._jsonMeta.filter)
		setDisabled(getEle(this.getSliderFieldId("")), disable);
};

RseField.prototype.update = function(jsonData) {
	if (this._jsonMeta.filter) {
		if (this._cleared || this._slider.getMaximum() != jsonData.sliderMax) 
			this._slider.setMaximum(jsonData.sliderMax);

		if (this._cleared || this._slider.getValue() != jsonData.sliderVal) 
			this._slider.setValue(jsonData.sliderVal);
	}
	if (this._cleared || this._jsonData.fieldVal != jsonData.fieldVal) {
		var imgSource = "";
		var isImage = (jsonData.fieldVal.match("\.(gif|jpg|jpeg|png)$") != null);
//		var isImage = (jsonData.fieldVal.match("^(/[_a-zA-Z0-9.-]+)*/[_a-zA-Z0-9.-]+\.(gif|jpg|jpeg|png)$") != null);
		if (isImage) {
			if (jsonData.fieldVal.match("^http://") == null) imgSource += "/imageServ?filename="; 
			imgSource += jsonData.fieldVal;
		}
		getEle(this.getValFieldId("img")).src = imgSource;
		getEle(this.getValFieldId("txt")).innerHTML = jsonData.fieldVal;
	}
	
	getEle(this.getVariantsFieldId()).innerHTML = (jsonData.sliderVal + 1) + "/" + (jsonData.sliderMax + 1);
	
	this._jsonData = jsonData;
	if (this._cleared) this._cleared = false;
};

