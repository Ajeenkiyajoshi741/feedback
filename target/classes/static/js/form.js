$(document).ready(function() {
  let questionNumber = 1; // Initialize question number

  $('#question-type').change(function() {
    var questionType = $(this).val();
    if (questionType === 'multiple-choice' || questionType === 'checkboxes' || questionType === 'dropdown') {
      $('#options-group').show();
      $('#scale-group').hide();
    } else if (questionType === 'linear-scale') {
      $('#options-group').hide();
      $('#scale-group').show();
    } else {
      $('#options-group').hide();
      $('#scale-group').hide();
    }
  });

  $('#add-question').click(function() {
    if ($('#generated-form .preview-question').length >= 5) {
      alert('You can only add up to 5 questions.');
      return;
    }

    var questionType = $('#question-type').val();
    var questionText = $('#question-text').val();
    var questionOptions = $('#question-options').val();
    var scaleStart = $('#scale-start').val();
    var scaleEnd = $('#scale-end').val();

    if (!questionText) {
      alert('Please enter a question.');
      return;
    }

    var newQuestion = `<div class="preview-question"><h3>Question ${questionNumber}: ${questionText}</h3><i class="fas fa-trash-alt delete-icon"></i>`;

    switch(questionType) {
      case 'short-answer':
        newQuestion += '<input type="text" style="width:90%;">';
        break;
      case 'paragraph':
        newQuestion += '<textarea style="width:90%;" rows="4"></textarea>';
        break;
      case 'multiple-choice':
      case 'checkboxes':
      case 'dropdown':
        if (!questionOptions) {
          alert('Please enter options for this question.');
          return;
        }
        var options = questionOptions.split('\n');
        if (questionType === 'multiple-choice') {
          options.forEach(function(option) {
            newQuestion += `<div><input type="radio" name="question${questionNumber}"> ${option}</div>`;
          });
        } else if (questionType === 'checkboxes') {
          options.forEach(function(option) {
            newQuestion += `<div><input type="checkbox" name="question${questionNumber}"> ${option}</div>`;
          });
        } else if (questionType === 'dropdown') {
          newQuestion += `<select>`;
          options.forEach(function(option) {
            newQuestion += `<option>${option}</option>`;
          });
          newQuestion += `</select>`;
        }
        break;
      case 'linear-scale':
        if (!scaleStart || !scaleEnd) {
          alert('Please enter scale start and end values.');
          return;
        }
        newQuestion += `<div><label>${scaleStart}</label>`;
        for (var i = scaleStart; i <= scaleEnd; i++) {
          newQuestion += `<input type="radio" name="question${questionNumber}"> ${i}`;
        }
        newQuestion += `<label>${scaleEnd}</label></div>`;
        break;
      case 'smiley-rating':
        newQuestion += `<div class="rating-smileys"><span>&#128546;</span><span>&#128542;</span><span>&#128528;</span><span>&#128578;</span><span>&#128516;</span></div>`;
        break;
      case 'star-rating':
        newQuestion += `<div class="rating-stars">`;
        for (var i = 1; i <= 5; i++) {
          newQuestion += `<span>&#9733;</span>`;
        }
        newQuestion += `</div>`;
        break;
    }

    newQuestion += '</div>';
    $('#generated-form').append(newQuestion);
    questionNumber++; // Increment question number

    $('#question-text').val('');
    $('#question-options').val('');
    $('#scale-start').val(1);
    $('#scale-end').val(5);
  });

  $(document).on('click', '.delete-icon', function() {
    $(this).closest('.preview-question').remove();
    questionNumber--; // Decrement question number
    updateQuestionNumbers(); // Update question numbers
  });

  function updateQuestionNumbers() {
    $('#generated-form .preview-question').each(function(index) {
      $(this).find('h3').text(`Question ${index + 1}: ` + $(this).find('h3').text().split(': ')[1]);
    });
  }

  $('#reset-button').click(function() {
    $('#generated-form').empty();
    questionNumber = 1;
  });
});