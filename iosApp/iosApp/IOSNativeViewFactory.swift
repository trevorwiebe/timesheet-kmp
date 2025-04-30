//
//  IOSNativeViewFactory.swift
//  iosApp
//
//  Created by Trevor Wiebe on 4/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    
    static var shared = IOSNativeViewFactory()

    
    func createDialog(
        confirmText: String,
        onConfirm: @escaping () -> Void,
        dismissText: String,
        onDismiss: @escaping () -> Void,
        title: String?,
        message: String?
    ) -> UIViewController {
        var host: UIHostingController<GeneralDialog>? = nil

        let view = GeneralDialog(
            confirmText: confirmText,
            onConfirm: {
                host?.dismiss(animated: true)
                onConfirm()
            },
            dismissText: dismissText,
            onDismiss: {
                host?.dismiss(animated: true)
                onDismiss()
            },
            title: title,
            message: message
        )

        let controller = UIHostingController(rootView: view)
        host = controller
        controller.modalPresentationStyle = .overFullScreen
        controller.modalTransitionStyle = .crossDissolve
        controller.view.backgroundColor = .clear

        if let rootVC = UIApplication.shared.windows.first(where: { $0.isKeyWindow })?.rootViewController {
            DispatchQueue.main.async {
                rootVC.present(controller, animated: true, completion: nil)
            }
        }

        return UIViewController()
    }
    
    
    func createTimePickerView(
        punch: Punch,
        onDismiss: @escaping () -> Void,
        onTimeSelected: @escaping (Punch) -> Void
    ) -> UIViewController {
        var host: UIHostingController<TimePicker>? = nil

        let view = TimePicker(
            punch: punch,
            onDismiss: {
                host?.dismiss(animated: true)
                onDismiss()
            },
            onTimeSelected: { punch in
                onTimeSelected(punch)
            }
        )

        let controller = UIHostingController(rootView: view)
        host = controller
        controller.modalPresentationStyle = .overFullScreen
        controller.modalTransitionStyle = .crossDissolve
        controller.view.backgroundColor = .clear

        if let rootVC = UIApplication.shared.windows.first(where: { $0.isKeyWindow })?.rootViewController {
            DispatchQueue.main.async {
                rootVC.present(controller, animated: true, completion: nil)
            }
        }

        return UIViewController()
    }
}
