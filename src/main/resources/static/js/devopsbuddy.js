$(document).ready(main);

function main() {
	$('.btn-collapse').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		var $collapse = $this.closest('.collapse-group').find('.collapse');
		$collapse.collapse('toggle');
	});
	
	/* Contact form Validation */
	$('#contactForm').formValidation({
		framework: 'bootstrap',
		icon: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			email: {
				validators: {
					notEmpty: {
						message: 'The email is required'
					},
					emailAddress:{
						message: 'The input is not a valid email address'
					}
				}
			},
			firstName: {
				validators: {
					notEmpty: {
						message: 'The first name is required'
					}
				}
			},
			lastName: {
				validators: {
					notEmpty: {
						message: 'The last name is required'
					}
				}
			},
			feedback: {
				validators: {
					notEmpty: {
						message: 'Your feedback is valued and required'
					}
				}
			}
		}
		
	});
	
	/* Forgot password form validation */
	$('#forgotPasswordForm').formValidation({
		framework: 'bootstrap',
		icon: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			email: {
				validators: {
					notEmpty: {
						message: 'The email is required'
					},
					emailAddress:{
						message: 'The input is not a valid email address'
					}
				}
			}
		}
	});	
	
	$('#savePasswordForm').formValidation({
		framework: 'bootstrap',
		icon: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			password: {
				validators: {
					notEmpty: {
						message: 'The password is required'
					},
					identical:{
						field: 'confirmPassword',
						message: 'The password and its confirmation are not the same'
					}
				}
			},
			confirmPassword: {
				validators: {
					notEmpty: {
						message: 'The confirmation password is required'
					},
					identical:{
						field: 'password',
						message: 'The password and its confirmation are not the same'
					}
				}
			}
		}
	});
}