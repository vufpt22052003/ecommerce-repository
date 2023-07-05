function handleFileSelect(event) {
	const file = event.target.files[0];
	const reader = new FileReader();
	reader.onload = function(event) {
		const img = document.createElement('img');
		img.setAttribute('src', event.target.result);
		img.classList.add('contain'); // Thêm class "image-class"
		const container = document.getElementById('image-container');
		container.innerHTML = '';
		container.appendChild(img);
	}
	reader.readAsDataURL(file);
}



const inputElement = document.getElementById("image-upload-2");
const previewElement = document.getElementById("image-preview");

inputElement.addEventListener("change", (event) => {
	//previewElement.innerHTML = "";
	const files = event.target.files;

	for (let i = 0; i < files.length; i++) {
		const file = files[i];
		const imageElement = document.createElement("img");

		imageElement.file = file;
		imageElement.classList.add("preview-image");

		previewElement.appendChild(imageElement);

		const reader = new FileReader();
		reader.onload = (function(aImg) {
			return function(e) {
				aImg.src = e.target.result;
			};
		})(imageElement);

		reader.readAsDataURL(file);
	}
});

// mau sac
// Mau sac
var addInputButton = document.getElementById('add-input');
var inputContainer = document.getElementById('input-container');
var indexCounter = inputContainer.children.length; // Global counter to keep track of the index
addInputButton.addEventListener('click', function() {
	var newInputWrapper = document.createElement('div'); // Create a wrapper div for each input and its delete button
	newInputWrapper.classList.add('input-wrapper'); // Add a class to identify input wrappers

	var newInput = document.createElement('input');
	newInput.setAttribute('type', 'text');
	newInput.classList.add('w3-input', 'w3-border', 'w3-round');
	newInput.setAttribute('name', 'color[]'); // Add the name attribute with value 'color[]'
	newInput.setAttribute('placeholder', 'Thêm Màu Sắc');

	var deleteSpan = document.createElement('span');
	deleteSpan.classList.add('delete-input');
	deleteSpan.setAttribute('data-index', indexCounter); // Assign the current index from the counter
	deleteSpan.innerHTML = '&times;';
	deleteSpan.onclick = deleteInput; // Add the onclick event handler here

	newInputWrapper.appendChild(newInput);
	newInputWrapper.appendChild(deleteSpan);
	inputContainer.appendChild(newInputWrapper);

	indexCounter++; // Increment the index counter for the next input

	// Add style "gap: 11px;" to the input container
	inputContainer.style.display = "flex";
	inputContainer.style.flexWrap = "wrap";
	inputContainer.style.gap = "11px";
});


function deleteInput(event) {
	event.preventDefault();
	const index = event.target.getAttribute("data-index");
	const inputWrapperList = inputContainer.getElementsByClassName("input-wrapper");

	if (inputWrapperList.length > 1) {
		inputWrapperList[index].remove();

		// Update the data-index attributes of the remaining input wrappers
		const remainingInputWrappers = document.getElementsByClassName("input-wrapper");
		for (let i = 0; i < remainingInputWrappers.length; i++) {
			remainingInputWrappers[i].querySelector(".delete-input").setAttribute("data-index", i);
		}
	}
}


// Size
var addSizeButton = document.getElementById('add-size');
var sizeContainer = document.getElementById('input-size');
var sizeIndexCounter = sizeContainer.children.length; // Global counter to keep track of the index

addSizeButton.addEventListener('click', function() {
	var newInputWrapper = document.createElement('div'); // Create a wrapper div for each input and its delete button
	newInputWrapper.classList.add('input-wrapper'); // Add a class to identify input wrappers

	var newInputSize = document.createElement('input');
	newInputSize.setAttribute('type', 'text');
	newInputSize.classList.add('w3-input', 'w3-border', 'w3-round');
	newInputSize.setAttribute('name', 'sizes[]');
	newInputSize.setAttribute('placeholder', 'Chọn Size');

	var deleteSpan = document.createElement('span');
	deleteSpan.classList.add('delete-input');
	deleteSpan.setAttribute('data-index', sizeIndexCounter); // Assign the current index from the counter
	deleteSpan.innerHTML = '&times;';
	deleteSpan.onclick = deleteInputSize; // Add the onclick event handler here

	newInputWrapper.appendChild(newInputSize);
	newInputWrapper.appendChild(deleteSpan);
	sizeContainer.appendChild(newInputWrapper);

sizeContainer.style.gap = "11px";

	sizeIndexCounter++; // Increment the index counter for the next input
});

function deleteInputSize(event) {
	event.preventDefault();
	const index = event.target.getAttribute('data-index');
	const inputWrapperList = sizeContainer.getElementsByClassName('input-wrapper');

	if (inputWrapperList.length > 1) {
		inputWrapperList[index].remove();

		// Update the data-index attributes of the remaining input wrappers
		const remainingInputWrappers = sizeContainer.getElementsByClassName('input-wrapper');
		for (let i = 0; i < remainingInputWrappers.length; i++) {
			remainingInputWrappers[i].querySelector('.delete-input').setAttribute('data-index', i);
		}
	}
}



var app = angular.module('admin', [])
app.controller('adminController', function($scope) {
	$scope.calculateTotal = function() {
		if ($scope.price > $scope.sale) {
			$scope.total = ($scope.price - $scope.sale) || 0;
		}
	}

	$scope.$watchGroup(['price', 'sale'], function(newValues, oldValues, scope) {
		$scope.calculateTotal();
	});


});

function updateSelectedOption() {
	var id = document.getElementById("inputVendor").value;
	document.getElementById("selectedOption").value = id;
	alert(id)
}


Chart.defaults.global.defaultFontColor = 'white';
let ctxLine, ctxBar, ctxPie, optionsLine, optionsBar, optionsPie, configLine, configBar, configPie, lineChart;
barChart, pieChart;
// DOM is ready
$(function() {
	drawLineChart(); // Line Chart
	drawBarChart(); // Bar Chart
	drawPieChart(); // Pie Chart

	$(window).resize(function() {
		updateLineChart();
		updateBarChart();
	});
})

function showInputs() {
	var inputs = document.querySelectorAll(".hidden-input");
	for (var i = 0; i < inputs.length; i++) {
		inputs[i].style.display = "block";
	}
}
