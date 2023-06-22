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
var addInputButton = document.getElementById('add-input');
var inputContainer = document.getElementById('input-container');

addInputButton.addEventListener('click', function() {
	var newInput = document.createElement('input');
	newInput.setAttribute('type', 'text');
	newInput.classList.add('w3-input', 'w3-border', 'w3-round');
	newInput.setAttribute('placeholder', 'Thêm Màu Sắc');
	inputContainer.appendChild(newInput);
});


// size
var addSizeButton = document.getElementById('add-size');
var sizeContainer = document.getElementById('input-size');

addSizeButton.addEventListener('click', function() {
	var newInputSize = document.createElement('input');
	newInputSize.setAttribute('type', 'text');
	newInputSize.setAttribute('name', 'sizes[]');
	newInputSize.setAttribute('placeholder', 'Chọn Size');
	newInputSize.classList.add('w3-input', 'w3-border', 'w3-round');


	sizeContainer.appendChild(newInputSize);
});



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
