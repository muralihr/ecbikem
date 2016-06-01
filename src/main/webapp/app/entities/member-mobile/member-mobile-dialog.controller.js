(function() {
    'use strict';

    angular
        .module('ecbikeApp')
        .controller('MemberMobileDialogController', MemberMobileDialogController);

    MemberMobileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MemberMobile'];

    function MemberMobileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MemberMobile) {
        var vm = this;
        vm.memberMobile = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('ecbikeApp:memberMobileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.memberMobile.id !== null) {
                MemberMobile.update(vm.memberMobile, onSaveSuccess, onSaveError);
            } else {
                MemberMobile.save(vm.memberMobile, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateOfBirth = false;

        vm.setPhoto = function ($file, memberMobile) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        memberMobile.photo = base64Data;
                        memberMobile.photoContentType = $file.type;
                    });
                });
            }
        };

        vm.setPhotoIDProof = function ($file, memberMobile) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        memberMobile.photoIDProof = base64Data;
                        memberMobile.photoIDProofContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.dateOfExpiration = false;

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
